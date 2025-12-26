package com.openmpy.server.document.application.response;

import java.util.List;

public record DocumentImageUploadResponses(
        List<DocumentImageUploadResponse> images
) {
}
