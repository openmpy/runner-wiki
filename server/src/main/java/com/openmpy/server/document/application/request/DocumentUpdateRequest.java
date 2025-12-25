package com.openmpy.server.document.application.request;

public record DocumentUpdateRequest(
        String author,
        String content
) {
}
