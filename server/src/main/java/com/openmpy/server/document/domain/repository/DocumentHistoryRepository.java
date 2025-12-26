package com.openmpy.server.document.domain.repository;

import com.openmpy.server.document.domain.model.DocumentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long> {

    Page<DocumentHistory> findAllByDocumentId(final Long documentId, final Pageable pageable);

    @Query(value = """
            select coalesce(max(h.version), 0)
            from document_history h
            where h.document_id = :documentId
            """, nativeQuery = true)
    long findMaxVersion(@Param("documentId") final Long documentId);
}
