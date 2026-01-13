package com.openmpy.server.document.application.query.dto.response;

import java.util.List;

public record DocumentTop10Response(
        List<DocumentPageResponse> documents
) {
}
