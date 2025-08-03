package com.openmpy.wiki.document.application.response;

import com.openmpy.wiki.document.domain.entity.DocumentHistory;
import java.time.LocalDateTime;

public record DocumentHistoryReadResponse(
        Long documentHistoryId,
        String author,
        Long version,
        Integer size,
        LocalDateTime createdAt
) {

    public static DocumentHistoryReadResponse from(final DocumentHistory documentHistory) {
        return new DocumentHistoryReadResponse(
                documentHistory.getId(),
                documentHistory.getAuthor(),
                documentHistory.getVersion(),
                documentHistory.getContent().length(),
                documentHistory.getCreatedAt()
        );
    }
}
