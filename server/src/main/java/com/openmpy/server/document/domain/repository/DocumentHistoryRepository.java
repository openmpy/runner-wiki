package com.openmpy.server.document.domain.repository;

import com.openmpy.server.document.domain.model.DocumentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long> {

    Page<DocumentHistory> findAllByDocumentId(final Long documentId, final Pageable pageable);
}
