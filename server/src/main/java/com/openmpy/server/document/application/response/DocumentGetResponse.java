package com.openmpy.server.document.application.response;

import com.openmpy.server.document.domain.constants.DocumentCategory;
import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.entity.DocumentHistory;
import java.time.LocalDateTime;

public record DocumentGetResponse(
        Long documentId,
        Long documentHistoryId,
        String title,
        DocumentCategory category,
        String author,
        String content,
        Long version,
        Long size,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt
) {

    public static DocumentGetResponse from(final Document document, final DocumentHistory documentHistory) {
        return new DocumentGetResponse(
                document.getId(),
                documentHistory.getId(),
                document.getTitle(),
                document.getCategory(),
                documentHistory.getAuthor(),
                documentHistory.getContent(),
                documentHistory.getVersion(),
                documentHistory.getSize(),
                document.getCreatedAt(),
                documentHistory.getCreatedAt()
        );
    }
}
