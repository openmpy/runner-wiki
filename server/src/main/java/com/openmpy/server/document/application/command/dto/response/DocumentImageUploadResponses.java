package com.openmpy.server.document.application.command.dto.response;

import java.util.List;

public record DocumentImageUploadResponses(
        List<DocumentImageUploadResponse> images
) {
}
