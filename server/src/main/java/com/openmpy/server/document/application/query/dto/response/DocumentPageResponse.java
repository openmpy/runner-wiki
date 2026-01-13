package com.openmpy.server.document.application.query.dto.response;

import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.type.DocumentCategory;
import java.time.LocalDateTime;

public record DocumentPageResponse(
        Long documentId,
        String title,
        DocumentCategory category,
        LocalDateTime lastModifiedAt
) {

    public static DocumentPageResponse from(final Document document) {
        return new DocumentPageResponse(
                document.getId(),
                document.getTitle(),
                document.getCategory(),
                document.getUpdatedAt()
        );
    }
}
