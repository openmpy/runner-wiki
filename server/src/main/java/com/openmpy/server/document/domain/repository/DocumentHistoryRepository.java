package com.openmpy.server.document.domain.repository;

import com.openmpy.server.document.domain.entity.DocumentHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long> {

    @Query(
        value = """
            SELECT
                dh.*
            FROM (
                SELECT id, version
                FROM document_history
                WHERE document_id = :documentId
                  AND deleted_at IS NULL
                ORDER BY version DESC, id DESC
                LIMIT :limit OFFSET :offset
            ) t
            JOIN document_history dh ON dh.id = t.id
            WHERE dh.deleted_at IS NULL
            ORDER BY t.version DESC, t.id DESC
            """,
        nativeQuery = true
    )
    List<DocumentHistory> findAllByDocumentId(
        @Param("documentId") Long documentId,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    @Query(
        value = """
            SELECT count(*)
            FROM (
                SELECT id
                FROM document_history
                WHERE document_id = :documentId
                  AND deleted_at IS NULL
                LIMIT :limit
            ) t
            """,
        nativeQuery = true
    )
    Long countByDocumentId(
        @Param("documentId") Long documentId,
        @Param("limit") int limit
    );

    @Query(
        value = """
            SELECT dh
            FROM DocumentHistory dh
            JOIN FETCH dh.document d
            WHERE dh.id = :id
            """
    )
    Optional<DocumentHistory> findByIdWithDocument(@Param("id") final Long id);

    List<DocumentHistory> findAllByDocument_Id(final Long documentId);

    @Query("select h.version.value from DocumentHistory h where h.document.id = :documentId")
    List<Integer> findAllVersionsByDocumentId(@Param("documentId") final Long documentId);
}
