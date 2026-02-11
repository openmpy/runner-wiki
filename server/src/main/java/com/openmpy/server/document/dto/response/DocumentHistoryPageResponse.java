package com.openmpy.server.document.dto.response;

import com.openmpy.server.document.domain.entity.DocumentHistory;
import java.time.LocalDateTime;

public record DocumentHistoryPageResponse(
    String title,
    Long documentId,
    Long documentHistoryId,
    String author,
    Integer version,
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
