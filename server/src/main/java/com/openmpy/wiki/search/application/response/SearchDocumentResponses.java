package com.openmpy.wiki.search.application.response;

import java.util.List;

public record SearchDocumentResponses(
        List<SearchDocumentResponse> items
) {

    public static SearchDocumentResponses of(final List<SearchDocumentResponse> indexes) {
        return new SearchDocumentResponses(indexes);
    }
}
