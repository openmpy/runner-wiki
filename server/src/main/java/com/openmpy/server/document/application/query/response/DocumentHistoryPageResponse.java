package com.openmpy.server.document.application.query.response;

import com.openmpy.server.document.domain.model.DocumentHistory;
import java.time.LocalDateTime;

public record DocumentHistoryPageResponse(
        Long documentHistoryId,
        String author,
        Long version,
        Long size,
        LocalDateTime createdAt
) {

    public static DocumentHistoryPageResponse from(final DocumentHistory documentHistory) {
        return new DocumentHistoryPageResponse(
                documentHistory.getId(),
                documentHistory.getAuthor(),
                documentHistory.getVersion(),
                documentHistory.getSize(),
                documentHistory.getCreatedAt()
        );
    }
}
