package com.openmpy.wiki.view.application;

import com.openmpy.wiki.document.domain.repository.DocumentRepository;
import com.openmpy.wiki.view.domain.entity.View;
import com.openmpy.wiki.view.domain.repository.ViewRepository;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ViewService {

    // document-view::documentId::clientIp
    private static final String DOCUMENT_VIEW_DUPLICATION_KEY = "document-view::%s::%s";
    // document-view-count::documentId
    private static final String DOCUMENT_VIEW_COUNT_KEY = "document-view-count::%s";
    // document-view-score::documentId
    private static final String DOCUMENT_VIEW_SCORE_KEY = "document-view-score";

    private final ViewRepository viewRepository;
    private final DocumentRepository documentRepository;
    private final StringRedisTemplate redisTemplate;

    @Transactional
    public void syncViewCount() {
        final String viewCountKey = String.format(DOCUMENT_VIEW_COUNT_KEY, "*");
        final Set<String> keys = redisTemplate.keys(viewCountKey);

        if (hasEmptyKeys(keys)) {
            return;
        }

        for (final String key : keys) {
            final String documentId = key.split("::")[1];
            final Long viewCount = getViewCount(documentId);

            viewRepository.findById(documentId).ifPresentOrElse(
                    it -> it.increment(viewCount),
                    () -> {
                        if (documentRepository.existsById(documentId)) {
                            final View view = View.init(documentId);
                            viewRepository.save(view);
                        }
                    }
            );
            redisTemplate.delete(key);
        }
    }

    @Transactional
    public void deleteView(final String documentId) {
        viewRepository.findById(documentId).ifPresent(viewRepository::delete);

        final String viewCountKey = String.format(DOCUMENT_VIEW_COUNT_KEY, documentId);
        redisTemplate.delete(viewCountKey);
    }

    public void incrementViewCount(final String documentId, final String clientIp) {
        final String duplicatedKey = String.format(DOCUMENT_VIEW_DUPLICATION_KEY, documentId, clientIp);
        final String viewCountKey = String.format(DOCUMENT_VIEW_COUNT_KEY, documentId);
        final Boolean duplicated = redisTemplate.opsForValue().setIfAbsent(
                duplicatedKey, "happy", Duration.ofMinutes(10)
        );

        if (Boolean.TRUE.equals(duplicated)) {
            redisTemplate.opsForValue().increment(viewCountKey);
            redisTemplate.opsForZSet().incrementScore(DOCUMENT_VIEW_SCORE_KEY, documentId, 1.0);
        }
    }

    public void decrementViewScore() {
        final Set<String> documentIds = redisTemplate.opsForZSet().range(DOCUMENT_VIEW_SCORE_KEY, 0, -1);

        if (hasEmptyKeys(documentIds)) {
            return;
        }

        for (final String documentId : documentIds) {
            final Double score = redisTemplate.opsForZSet().score(DOCUMENT_VIEW_SCORE_KEY, documentId);
            if (score == null) {
                continue;
            }

            final double decayedScore = score * 0.9;

            if (decayedScore < 0.5) {
                redisTemplate.opsForZSet().remove(DOCUMENT_VIEW_SCORE_KEY, documentId);
            } else {
                redisTemplate.opsForZSet().add(DOCUMENT_VIEW_SCORE_KEY, documentId, decayedScore);
            }
        }
    }

    public List<String> getViewScore() {
        final Set<String> viewIds = redisTemplate.opsForZSet().reverseRange(DOCUMENT_VIEW_SCORE_KEY, 0, 9);
        if (hasEmptyKeys(viewIds)) {
            return List.of();
        }

        return List.copyOf(viewIds);
    }

    private boolean hasEmptyKeys(final Set<String> keys) {
        return keys == null || keys.isEmpty();
    }

    private Long getViewCount(final String documentId) {
        final String viewCountKey = String.format(DOCUMENT_VIEW_COUNT_KEY, documentId);
        return Long.valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get(viewCountKey)));
    }
}
