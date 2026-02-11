package com.openmpy.server.global.util;

import java.nio.charset.StandardCharsets;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ContentCalculator {

    public static long calculateUtf8Bytes(final String content) {
        if (content == null || content.isBlank()) {
            return 0L;
        }
        return content.getBytes(StandardCharsets.UTF_8).length;
    }
}
