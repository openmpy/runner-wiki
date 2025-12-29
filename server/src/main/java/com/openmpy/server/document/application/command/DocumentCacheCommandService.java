package com.openmpy.server.document.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DocumentCacheCommandService {

    private static final String RANK_KEY = "rank:document:views";

    private final StringRedisTemplate redisTemplate;

    public void removeFromRanking(final Long documentId) {
        redisTemplate.opsForZSet().remove(RANK_KEY, String.valueOf(documentId));
    }
}
