package com.openmpy.server.document.application.query;

import static java.util.HashMap.newHashMap;

import com.openmpy.server.document.application.query.response.DocumentPageResponse;
import com.openmpy.server.document.application.query.response.DocumentTop10Response;
import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.repository.DocumentRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentCacheQueryService {

    private static final int MAX_TOP_DOCUMENTS = 10;
    private static final Duration DUPLICATE_BLOCK_TTL = Duration.ofMinutes(10);
    private static final Duration RANK_KEY_TTL = Duration.ofDays(8);
    private static final String INCR_IF_ALLOWED_LUA = """
            -- KEYS[1] = blockKey
            -- KEYS[2] = rankKey (zset)
            -- ARGV[1] = blockTtlSeconds
            -- ARGV[2] = member (documentId as string)
            -- ARGV[3] = rankKeyTtlSeconds
            
            local ok = redis.call('SET', KEYS[1], '1', 'NX', 'EX', ARGV[1])
            if not ok then
              return 0
            end
            
            redis.call('ZINCRBY', KEYS[2], 1, ARGV[2])
            
            -- set rank key ttl only if not set (newly created or persisted)
            local ttl = redis.call('TTL', KEYS[2])
            if ttl < 0 then
              redis.call('EXPIRE', KEYS[2], ARGV[3])
            end
            
            return 1
            """;

    private final StringRedisTemplate redisTemplate;
    private final DocumentRepository documentRepository;
    private final DefaultRedisScript<Long> increaseScript = script();

    public void increaseRankIfAllowed(final Long documentId, final String ip) {
        final String blockKey = duplicatedBlockKey(documentId, ip);
        final String rankKey = rankKeyOfToday();

        redisTemplate.execute(
                increaseScript,
                List.of(blockKey, rankKey),
                String.valueOf(DUPLICATE_BLOCK_TTL.toSeconds()),
                String.valueOf(documentId),
                String.valueOf(RANK_KEY_TTL.toSeconds())
        );
    }

    @Transactional(readOnly = true)
    public DocumentTop10Response getDocumentTop10() {
        final String rankKey = rankKeyOfToday();

        final int fetchSize = 80;
        final Set<String> rawIds = redisTemplate.opsForZSet().reverseRange(rankKey, 0, fetchSize - 1);

        if (rawIds == null || rawIds.isEmpty()) {
            return new DocumentTop10Response(List.of());
        }

        final List<Long> rankedIds = rawIds.stream().map(Long::parseLong).toList();
        final List<Document> documents = documentRepository.findAllByIdIn(rankedIds);

        final Map<Long, Integer> orderIndex = newHashMap(rankedIds.size() * 2);
        for (int i = 0; i < rankedIds.size(); i++) {
            orderIndex.put(rankedIds.get(i), i);
        }

        final List<Document> sorted = documents.stream()
                .sorted(Comparator.comparingInt(d -> orderIndex.getOrDefault(d.getId(), Integer.MAX_VALUE)))
                .limit(MAX_TOP_DOCUMENTS)
                .toList();

        cleanupInvalidMembers(rankKey, rankedIds, documents);

        final List<DocumentPageResponse> responses = sorted.stream()
                .map(DocumentPageResponse::from)
                .toList();

        return new DocumentTop10Response(responses);
    }

    private DefaultRedisScript<Long> script() {
        final DefaultRedisScript<Long> script = new DefaultRedisScript<>();

        script.setScriptText(INCR_IF_ALLOWED_LUA);
        script.setResultType(Long.class);
        return script;
    }

    private String duplicatedBlockKey(final Long documentId, final String ip) {
        return "rank:duplicate-block:document:" + documentId + ":ip:" + sha256Hex(ip);
    }

    private String rankKeyOfToday() {
        return "rank:document:views:" + LocalDate.now(ZoneId.of("Asia/Seoul"));
    }

    private void cleanupInvalidMembers(
            final String rankKey,
            final List<Long> rankedIds,
            final List<Document> foundDocuments
    ) {
        final Set<Long> foundIds = foundDocuments.stream()
                .map(Document::getId)
                .collect(Collectors.toSet());

        final List<String> invalidMembers = rankedIds.stream()
                .filter(id -> !foundIds.contains(id))
                .limit(30)
                .map(String::valueOf)
                .toList();

        if (!invalidMembers.isEmpty()) {
            redisTemplate.opsForZSet().remove(rankKey, invalidMembers.toArray());
        }
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
            return Integer.toHexString(input.hashCode());
        }
    }
}
