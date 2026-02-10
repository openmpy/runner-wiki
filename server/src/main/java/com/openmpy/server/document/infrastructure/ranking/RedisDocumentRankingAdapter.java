package com.openmpy.server.document.infrastructure.ranking;

import com.openmpy.server.document.application.port.DocumentRankingPort;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisDocumentRankingAdapter implements DocumentRankingPort {

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
    private final DefaultRedisScript<Long> increaseScript = script();

    @Override
    public void removeFromRanking(final Long documentId) {
        final String rankKey = rankKeyOfToday();

        redisTemplate.opsForZSet().remove(rankKey, String.valueOf(documentId));
    }

    @Override
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

    @Override
    public Set<String> getTopRankedIds(final int fetchSize) {
        final String rankKey = rankKeyOfToday();

        return redisTemplate.opsForZSet().reverseRange(rankKey, 0, fetchSize - 1);
    }

    @Override
    public void removeInvalidMembers(final String rankKey, final Object[] members) {
        redisTemplate.opsForZSet().remove(rankKey, members);
    }

    @Override
    public String duplicatedBlockKey(final Long documentId, final String ip) {
        return "rank:duplicate-block:document:" + documentId + ":ip:" + sha256Hex(ip);
    }

    @Override
    public String rankKeyOfToday() {
        return "rank:document:views:" + LocalDate.now(ZoneId.of("Asia/Seoul"));
    }

    private DefaultRedisScript<Long> script() {
        final DefaultRedisScript<Long> script = new DefaultRedisScript<>();

        script.setScriptText(INCR_IF_ALLOWED_LUA);
        script.setResultType(Long.class);
        return script;
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
