package com.openmpy.server.document.application.response;

public record DocumentImageUploadResponse(
        Long imageId,
        String url
) {
}
