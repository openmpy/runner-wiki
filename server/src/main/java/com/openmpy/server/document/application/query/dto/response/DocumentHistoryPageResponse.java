package com.openmpy.server.document.application.query.dto.response;

import com.openmpy.server.document.domain.model.DocumentHistory;
import java.time.LocalDateTime;

public record DocumentHistoryPageResponse(
        String title,
        Long documentId,
        Long documentHistoryId,
        String author,
        Long version,
        Long size,
        LocalDateTime createdAt
) {

    public static DocumentHistoryPageResponse from(final DocumentHistory documentHistory) {
        return new DocumentHistoryPageResponse(
                documentHistory.getDocument().getTitle(),
                documentHistory.getDocument().getId(),
                documentHistory.getId(),
                documentHistory.getAuthor(),
                documentHistory.getVersion(),
                documentHistory.getSize(),
                documentHistory.getCreatedAt()
        );
    }
}
