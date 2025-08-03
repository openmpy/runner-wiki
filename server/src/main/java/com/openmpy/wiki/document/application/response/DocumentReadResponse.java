package com.openmpy.wiki.document.application.response;

import com.openmpy.wiki.document.domain.entity.Document;
import com.openmpy.wiki.document.domain.entity.DocumentHistory;
import java.time.LocalDateTime;

public record DocumentReadResponse(
        Long documentId,
        Long documentHistoryId,
        String title,
        String category,
        String author,
        String content,
        LocalDateTime createdAt
) {

    public static DocumentReadResponse from(final Document document, final DocumentHistory documentHistory) {
        return new DocumentReadResponse(
                document.getId(),
                documentHistory.getId(),
                document.getTitle(),
                document.getCategory().getValue(),
                documentHistory.getAuthor(),
                documentHistory.getContent(),
                documentHistory.getCreatedAt()
        );
    }
}
