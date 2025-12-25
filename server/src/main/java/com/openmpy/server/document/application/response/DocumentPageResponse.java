package com.openmpy.server.document.application.response;

import com.openmpy.server.document.domain.constants.DocumentCategory;
import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.entity.DocumentHistory;
import java.time.LocalDateTime;

public record DocumentPageResponse(
        Long documentId,
        String title,
        DocumentCategory category,
        LocalDateTime lastModifiedAt
) {

    public static DocumentPageResponse from(final Document document) {
        final DocumentHistory lastHistory = document.getLastHistory();

        return new DocumentPageResponse(
                document.getId(),
                document.getTitle(),
                document.getCategory(),
                lastHistory.getCreatedAt()
        );
    }
}
