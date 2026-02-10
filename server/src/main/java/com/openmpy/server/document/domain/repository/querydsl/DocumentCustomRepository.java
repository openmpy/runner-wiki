package com.openmpy.server.document.domain.repository.querydsl;

import com.openmpy.server.document.dto.response.DocumentGetResponse;

public interface DocumentCustomRepository {

    DocumentGetResponse findLatestDocumentById(final Long documentId);
}
