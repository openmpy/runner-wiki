package com.openmpy.server.document.application.query.port;

import com.openmpy.server.document.domain.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentSearchRepository {

    Page<Document> searchByChosungPrefix(final String query, final Pageable pageable);

    Page<Document> searchByTitlePrefix(final String query, final Pageable pageable);

    Page<Document> searchByTrgm(final String query, final Pageable pageable);
}
