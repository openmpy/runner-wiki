package com.openmpy.server.document.domain.repository.projection;

import java.time.LocalDateTime;

public interface DocumentHistoryPageRow {

    Long getDocumentId();

    String getDocumentTitle();

    Long getHistoryId();

    String getAuthor();

    Integer getVersion();

    Long getSize();

    LocalDateTime getCreatedAt();
}
