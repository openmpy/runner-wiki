package com.openmpy.server.document.domain.repository;

import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.type.DocumentCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    boolean existsByTitle_ValueAndCategory(final String title, final DocumentCategory category);

    Page<Document> findAllByCategory(final DocumentCategory category, final Pageable pageable);

    Page<Document> findAllByTitle_ValueContainingIgnoreCase(final String title, final Pageable pageable);
}
