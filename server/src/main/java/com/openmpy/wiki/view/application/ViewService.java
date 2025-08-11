package com.openmpy.wiki.view.application;

import com.openmpy.wiki.view.domain.entity.View;
import com.openmpy.wiki.view.domain.repository.ViewRepository;
import java.time.Duration;
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

    private final ViewRepository viewRepository;
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

            viewRepository.findById(documentId).ifPresentOrElse(it -> it.increment(viewCount),
                    () -> {
                        final View view = View.init(documentId);
                        viewRepository.save(view);
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
        }
    }

    private boolean hasEmptyKeys(final Set<String> keys) {
        return keys == null || keys.isEmpty();
    }

    private Long getViewCount(final String documentId) {
        final String viewCountKey = String.format(DOCUMENT_VIEW_COUNT_KEY, documentId);
        return Long.valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get(viewCountKey)));
    }
}
