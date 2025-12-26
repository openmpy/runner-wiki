package com.openmpy.server.document.application.command.response;

public record DocumentImageUploadResponse(
        Long imageId,
        String url
) {
}
