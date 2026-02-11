package com.openmpy.server.document.domain.repository;

import com.openmpy.server.document.domain.entity.DocumentHistory;
import com.openmpy.server.document.domain.repository.projection.DocumentHistoryPageRow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long> {

    @Query(
        value = """
            SELECT
                d.id AS documentId,
                d.title AS documentTitle,
                dh.id AS historyId,
                dh.author AS author,
                dh.version AS version,
                dh.size AS size,
                dh.created_at AS createdAt
            FROM (
                SELECT id, version
                FROM document_history
                WHERE document_id = :documentId
                  AND is_deleted = FALSE
                ORDER BY version DESC, id DESC
                LIMIT :limit OFFSET :offset
            ) t
            JOIN document_history dh ON dh.id = t.id
            JOIN document d ON d.id = dh.document_id
            ORDER BY t.version DESC, t.id DESC
            """,
        nativeQuery = true
    )
    List<DocumentHistoryPageRow> findAllByDocumentId(
        @Param("documentId") final Long documentId,
        @Param("offset") final int offset,
        @Param("limit") final int limit
    );

    @Query(
        value = """
            SELECT count(*)
            FROM (
                SELECT id
                FROM document_history
                WHERE document_id = :documentId
                  AND is_deleted = FALSE
                LIMIT :limit
            ) t
            """,
        nativeQuery = true
    )
    Long countByDocumentId(
        @Param("documentId") final Long documentId,
        @Param("limit") final int limit
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

    @Query("SELECT h.version.value FROM DocumentHistory h WHERE h.document.id = :documentId")
    List<Integer> findAllVersionsByDocumentId(@Param("documentId") final Long documentId);
}
