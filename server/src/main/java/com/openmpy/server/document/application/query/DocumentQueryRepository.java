package com.openmpy.server.document.application.query;

import com.openmpy.server.document.application.query.response.DocumentGetResponse;

public interface DocumentQueryRepository {

    DocumentGetResponse findLatestDocumentById(final Long documentId);
}
