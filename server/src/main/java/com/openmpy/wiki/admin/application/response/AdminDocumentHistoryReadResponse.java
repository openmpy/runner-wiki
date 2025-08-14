package com.openmpy.wiki.admin.application.response;

import com.openmpy.wiki.document.domain.entity.DocumentHistory;
import java.time.LocalDateTime;

public record AdminDocumentHistoryReadResponse(
        String documentHistoryId,
        String title,
        String author,
        String version,
        Integer size,
        String clientIp,
        LocalDateTime createdAt
) {

    public static AdminDocumentHistoryReadResponse from(final DocumentHistory documentHistory) {
        return new AdminDocumentHistoryReadResponse(
                documentHistory.getId(),
                documentHistory.getDocument().getTitle(),
                documentHistory.getAuthor(),
                String.valueOf(documentHistory.getVersion()),
                documentHistory.getContent().length(),
                documentHistory.getClientIp(),
                documentHistory.getCreatedAt()
        );
    }
}
