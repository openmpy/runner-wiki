package com.openmpy.server.document.dto.response;

import java.util.List;

public record DocumentTop10Response(
    List<DocumentPageResponse> documents
) {

}
