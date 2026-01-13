package com.openmpy.server.document.application.query.port;

import com.openmpy.server.document.application.query.dto.response.DocumentGetResponse;

public interface DocumentQueryRepository {

    DocumentGetResponse findLatestDocumentById(final Long documentId);
}
