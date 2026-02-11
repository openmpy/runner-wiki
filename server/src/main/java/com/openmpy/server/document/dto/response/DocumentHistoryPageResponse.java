package com.openmpy.server.document.dto.response;

import com.openmpy.server.document.domain.repository.projection.DocumentHistoryPageRow;
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

    public static DocumentHistoryPageResponse from(final DocumentHistoryPageRow row) {
        return new DocumentHistoryPageResponse(
            row.getDocumentTitle(),
            row.getDocumentId(),
            row.getHistoryId(),
            row.getAuthor(),
            row.getVersion(),
            row.getSize(),
            row.getCreatedAt()
        );
    }
}
