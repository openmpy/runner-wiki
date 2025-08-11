package com.openmpy.wiki.search.application.response;

import java.util.List;

public record SearchIndexResponses(
        List<SearchIndexResponse> indexes
) {

    public static SearchIndexResponses of(final List<SearchIndexResponse> indexes) {
        return new SearchIndexResponses(indexes);
    }
}
