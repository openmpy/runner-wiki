package com.openmpy.server.document.application.support;

import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

@Component
public class ContentSizeCalculator {

    public long calculateUtf8Bytes(final String content) {
        if (content == null) {
            return 0L;
        }
        return content.getBytes(StandardCharsets.UTF_8).length;
    }
}
