package com.openmpy.server.document.application.query;

import com.openmpy.server.document.application.query.response.DocumentPageResponse;
import com.openmpy.server.document.application.query.response.DocumentTop10Response;
import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentCacheQueryService {

    private static final int MAX_TOP_DOCUMENTS = 10;
    private static final Duration DUPLICATE_BLOCK_TTL = Duration.ofMinutes(MAX_TOP_DOCUMENTS);
    private static final String RANK_KEY = "rank:document:views";

    private final StringRedisTemplate redisTemplate;
    private final DocumentRepository documentRepository;

    public void increaseRankIfAllowed(final Long documentId, final String ip) {
        final String blockKey = duplicatedBlockKey(documentId, ip);
        final Boolean first = redisTemplate.opsForValue().setIfAbsent(blockKey, "1", DUPLICATE_BLOCK_TTL);

        if (!Boolean.TRUE.equals(first)) {
            return;
        }

        redisTemplate.opsForZSet().incrementScore(RANK_KEY, String.valueOf(documentId), 1.0);
    }

    @Transactional(readOnly = true)
    public DocumentTop10Response getDocumentTop10() {
        final Set<String> ids = redisTemplate.opsForZSet().reverseRange(RANK_KEY, 0, MAX_TOP_DOCUMENTS - 1);

        if (ids.isEmpty()) {
            return new DocumentTop10Response(List.of());
        }

        final List<Long> documentIds = ids.stream().map(Long::parseLong).toList();
        final List<Document> documents = documentRepository.findAllByIdIn(documentIds);
        final List<DocumentPageResponse> responses = documents.stream()
                .map(DocumentPageResponse::from)
                .toList();

        return new DocumentTop10Response(responses);
    }

    private String duplicatedBlockKey(final Long documentId, final String ip) {
        return "rank:duplicate-block:document:" + documentId + ":ip:" + sha256Hex(ip);
    }

    private String sha256Hex(final String input) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            final byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            final StringBuilder sb = new StringBuilder(digest.length * 2);

            for (final byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (final Exception e) {
            return input.replace(":", "_");
        }
    }
}
