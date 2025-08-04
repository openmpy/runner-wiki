package com.openmpy.wiki.document.application.request;

import java.util.List;

public record DocumentCreateRequest(
        String title,
        String category,
        String author,
        String content,
        List<Long> imageIds
) {
}
