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

    public static DocumentGetResponse of(final Document document) {
        final DocumentHistory lastHistory = document.getLastHistory();

        return new DocumentGetResponse(
                document.getId(),
                lastHistory.getId(),
                document.getTitle(),
                document.getCategory(),
                lastHistory.getAuthor(),
                lastHistory.getContent(),
                lastHistory.getVersion(),
                lastHistory.getSize(),
                document.getCreatedAt(),
                lastHistory.getCreatedAt()
        );
    }
}
