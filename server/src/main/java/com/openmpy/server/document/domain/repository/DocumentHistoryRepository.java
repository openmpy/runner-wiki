package com.openmpy.server.document.domain.repository;

import com.openmpy.server.document.domain.model.DocumentHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long> {

    @Query(
            value = "SELECT dh.id, dh.document_id, dh.author, dh.content, dh.version, dh.size, dh.client_ip, " +
                    "dh.created_at, dh.updated_at, dh.deleted_at " +
                    "FROM ( " +
                    "    SELECT id " +
                    "    FROM document_history " +
                    "    WHERE document_id = :documentId " +
                    "      AND deleted_at IS NULL " +
                    "    ORDER BY version DESC " +
                    "    LIMIT :limit OFFSET :offset " +
                    ") t " +
                    "JOIN document_history dh " +
                    "  ON dh.id = t.id " +
                    "WHERE dh.deleted_at IS NULL",
            nativeQuery = true
    )
    List<DocumentHistory> findAllByDocumentId(
            @Param("documentId") final Long documentId,
            @Param("offset") final int offset,
            @Param("limit") final int limit
    );


    @Query(
            value = "SELECT count(*) " +
                    "FROM ( " +
                    "  SELECT id " +
                    "  FROM document_history " +
                    "  WHERE document_id = :documentId " +
                    "    AND deleted_at IS NULL " +
                    "  ORDER BY version DESC " +
                    "  LIMIT :limit " +
                    ") t",
            nativeQuery = true
    )
    Long countByDocumentId(
            @Param("documentId") final Long documentId,
            @Param("limit") final int limit
    );

    @Query("""
            SELECT dh
            FROM DocumentHistory dh
            JOIN FETCH dh.document d
            WHERE dh.id = :id
            """)
    Optional<DocumentHistory> findByIdWithDocument(@Param("id") final Long id);

}
