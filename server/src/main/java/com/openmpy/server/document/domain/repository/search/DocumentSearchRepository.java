package com.openmpy.server.document.domain.repository.search;

import com.openmpy.server.document.domain.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentSearchRepository {

    Page<Document> searchByChosungPrefix(final String query, final Pageable pageable);

    Page<Document> searchByTitlePrefix(final String query, final Pageable pageable);

    Page<Document> searchByTrgm(final String query, final Pageable pageable);
}
