package com.openmpy.server.document.domain.repository;

import com.openmpy.server.document.domain.constants.DocumentCategory;
import com.openmpy.server.document.domain.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    boolean existsByTitleAndCategory(final String title, final DocumentCategory category);
}
