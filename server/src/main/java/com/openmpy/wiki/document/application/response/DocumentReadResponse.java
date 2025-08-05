package com.openmpy.wiki.document.application.response;

import com.openmpy.wiki.document.domain.entity.Document;
import com.openmpy.wiki.document.domain.entity.DocumentHistory;
import java.time.LocalDateTime;

public record DocumentReadResponse(
        String documentId,
        String documentHistoryId,
        String title,
        String category,
        String author,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static DocumentReadResponse from(final Document document) {
        return new DocumentReadResponse(
                document.getId().toString(),
                null,
                document.getTitle(),
                document.getCategory().getValue(),
                null,
                null,
                null,
                document.getUpdatedAt()
        );
    }

    public static DocumentReadResponse from(final Document document, final DocumentHistory documentHistory) {
        return new DocumentReadResponse(
                document.getId().toString(),
                documentHistory.getId().toString(),
                document.getTitle(),
                document.getCategory().getValue(),
                documentHistory.isDeleted() ? null : documentHistory.getAuthor(),
                documentHistory.isDeleted() ? null : documentHistory.getContent(),
                documentHistory.getCreatedAt(),
                document.getUpdatedAt()
        );
    }
}
