package com.openmpy.wiki.document.domain.repository;

import com.openmpy.wiki.document.domain.entity.Document;
import com.openmpy.wiki.document.domain.entity.DocumentHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, String> {

    Optional<DocumentHistory> findFirstByDocumentAndDeletedFalseOrderByVersionDesc(final Document document);

    Page<DocumentHistory> findAllByDocumentAndDeletedFalse(final Document document, final Pageable pageable);

    @Query("""
            SELECT dh 
            FROM DocumentHistory dh
            JOIN FETCH dh.document d
            WHERE d.id = :documentId 
              AND dh.deleted = false
            ORDER BY dh.version DESC
            """)
    List<DocumentHistory> findLatestHistoryWithDocument(
            @Param("documentId") final String documentId, final Pageable pageable
    );
}
