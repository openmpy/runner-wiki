package com.openmpy.server.document.domain.repository;

import com.openmpy.server.document.application.response.DocumentGetResponse;

public interface DocumentQueryRepository {

    DocumentGetResponse findLatestDocumentById(final Long documentId);
}
