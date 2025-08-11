package com.openmpy.wiki.search.application.response;

import com.meilisearch.sdk.Index;

public record SearchIndexResponse(
        String uid, String createdAt, String updatedAt, String primaryKey
) {

    public static SearchIndexResponse from(final Index index) {
        return new SearchIndexResponse(
                index.getUid(), index.getCreatedAt(), index.getUpdatedAt(), index.getPrimaryKey()
        );
    }
}
