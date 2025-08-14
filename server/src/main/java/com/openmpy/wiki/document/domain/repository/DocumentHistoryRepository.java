package com.openmpy.wiki.document.domain.repository;

import com.openmpy.wiki.document.domain.entity.Document;
import com.openmpy.wiki.document.domain.entity.DocumentHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, String> {

    @Query(
            value = "SELECT dh.id, dh.document_id, dh.author, dh.client_ip, dh.content, dh.created_at, dh.deleted, dh.version "
                    +
                    "FROM (" +
                    "  SELECT id FROM document_history " +
                    "  WHERE document_id = :documentId AND deleted = false " +
                    "  ORDER BY version DESC " +
                    "  LIMIT :limit OFFSET :offset" +
                    ") t LEFT JOIN document_history dh ON t.id = dh.id",
            nativeQuery = true
    )
    List<DocumentHistory> findAllByDocumentAndDeletedFalse(
            @Param("documentId") final String documentId,
            @Param("offset") final int offset,
            @Param("limit") final int limit
    );

    @Query(
            value = "SELECT COUNT(*) " +
                    "FROM (" +
                    "  SELECT id FROM document_history " +
                    "  WHERE document_id = :documentId AND deleted = false " +
                    "  ORDER BY version DESC " +
                    "  LIMIT :limit" +
                    ") t",
            nativeQuery = true
    )
    Long countByDocumentAndDeletedFalse(
            @Param("documentId") final String documentId,
            @Param("limit") final int limit
    );

    @Query(
            value = "SELECT dh.id, dh.document_id, dh.author, dh.client_ip, dh.content, dh.created_at, dh.deleted, dh.version, d.title "
                    +
                    "FROM (" +
                    "  SELECT id FROM document_history " +
                    "  ORDER BY created_at DESC " +
                    "  LIMIT :limit OFFSET :offset" +
                    ") t " +
                    "LEFT JOIN document_history dh ON t.id = dh.id " +
                    "LEFT JOIN document d ON dh.document_id = d.id",
            nativeQuery = true
    )
    List<DocumentHistory> findAll(
            @Param("offset") final int offset,
            @Param("limit") final int limit
    );

    @Query(
            value = "SELECT COUNT(*) " +
                    "FROM (" +
                    "  SELECT id FROM document_history " +
                    "  ORDER BY created_at DESC " +
                    "  LIMIT :limit" +
                    ") t",
            nativeQuery = true
    )
    Long count(
            @Param("limit") final int limit
    );

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

    @Query("""
            SELECT dh
            FROM DocumentHistory dh
            JOIN FETCH dh.document d
            WHERE dh.id = :documentHistoryId
            """)
    Optional<DocumentHistory> findByIdWithDocument(@Param("documentHistoryId") final String documentHistoryId);

    @Query("SELECT COALESCE(MAX(dh.version), 1) FROM DocumentHistory dh WHERE dh.document = :document")
    Long getLatestVersionByDocumentId(final Document document);
}
