package com.openmpy.wiki.document.application.response;

import com.openmpy.wiki.document.domain.entity.DocumentHistory;
import java.time.LocalDateTime;

public record DocumentHistoryReadResponse(
        String documentHistoryId,
        String author,
        String version,
        Integer size,
        LocalDateTime createdAt
) {

    public static DocumentHistoryReadResponse from(final DocumentHistory documentHistory) {
        return new DocumentHistoryReadResponse(
                documentHistory.getId(),
                documentHistory.getAuthor(),
                String.valueOf(documentHistory.getVersion()),
                documentHistory.getContent().length(),
                documentHistory.getCreatedAt()
        );
    }
}
