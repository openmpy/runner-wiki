package com.openmpy.server.document.domain.repository;

import com.openmpy.server.document.domain.model.DocumentHistory;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long> {

    @EntityGraph(attributePaths = "document")
    Page<DocumentHistory> findAllByDocumentId(final Long documentId, final Pageable pageable);

    @Query("""
            SELECT dh
            FROM DocumentHistory dh
            JOIN FETCH dh.document d
            WHERE dh.id = :id
            """)
    Optional<DocumentHistory> findByIdWithDocument(@Param("id") final Long id);

}
