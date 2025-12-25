package com.openmpy.server.document.domain.repository;

import com.openmpy.server.document.domain.constants.DocumentCategory;
import com.openmpy.server.document.domain.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    boolean existsByTitleAndCategory(final String title, final DocumentCategory category);

    Page<Document> findAllByCategory(final DocumentCategory category, final Pageable pageable);
}
