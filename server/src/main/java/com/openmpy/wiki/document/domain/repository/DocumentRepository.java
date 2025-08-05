package com.openmpy.wiki.document.domain.repository;

import com.openmpy.wiki.document.domain.constants.DocumentCategory;
import com.openmpy.wiki.document.domain.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, String> {

    boolean existsByTitle_ValueAndCategory(final String title, final DocumentCategory category);
}
