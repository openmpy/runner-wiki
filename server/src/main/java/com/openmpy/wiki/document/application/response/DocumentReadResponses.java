package com.openmpy.wiki.document.application.response;

import java.util.List;

public record DocumentReadResponses(
        List<DocumentReadResponse> items
) {
}
