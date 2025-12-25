package com.openmpy.server.document.application.request;

import com.openmpy.server.document.domain.constants.DocumentCategory;

public record DocumentCreateRequest(
        String title,
        DocumentCategory category,
        String author,
        String content
) {
}
