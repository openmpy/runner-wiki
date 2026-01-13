package com.openmpy.server.document.application.command.dto.response;

public record DocumentImageUploadResponse(
        Long imageId,
        String url
) {
}
