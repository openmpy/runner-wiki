package com.openmpy.server.document.domain.repository;

import com.openmpy.server.document.domain.entity.Document;
import com.openmpy.server.document.domain.repository.querydsl.DocumentCustomRepository;
import com.openmpy.server.document.domain.type.DocumentCategory;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentRepository extends
    JpaRepository<Document, Long>,
    DocumentCustomRepository {

    boolean existsByTitle_ValueAndCategory(final String title, final DocumentCategory category);

    @Query(
        value = """
            SELECT
                d.*
            FROM (
                SELECT id, updated_at
                FROM document
                WHERE is_deleted = FALSE
                ORDER BY updated_at DESC, id DESC
                LIMIT :limit OFFSET :offset
            ) t
            JOIN document d ON d.id = t.id
            ORDER BY t.updated_at DESC, t.id DESC
            """,
        nativeQuery = true
    )
    List<Document> findAllOrderByUpdatedAtDesc(
        @Param("offset") final int offset,
        @Param("limit") final int limit
    );

    @Query(
        value = """
            SELECT
                d.*
            FROM (
                SELECT id, updated_at
                FROM document
                WHERE category = :category
                  AND is_deleted = FALSE
                ORDER BY updated_at DESC, id DESC
                LIMIT :limit OFFSET :offset
            ) t
            JOIN document d ON d.id = t.id
            ORDER BY t.updated_at DESC, t.id DESC
            """,
        nativeQuery = true
    )
    List<Document> findAllByCategoryOrderByUpdatedAtDesc(
        @Param("category") final String category,
        @Param("offset") final int offset,
        @Param("limit") final int limit
    );

    @Query(
        value = """
            SELECT count(*)
            FROM (
                SELECT id
                FROM document
                WHERE is_deleted = FALSE
                LIMIT :limit
            ) t
            """,
        nativeQuery = true
    )
    Long count(
        @Param("limit") final int limit
    );

    @Query(
        value = """
            SELECT count(*)
            FROM (
                SELECT id
                FROM document
                WHERE category = :category
                  AND is_deleted = FALSE
                LIMIT :limit
            ) t
            """,
        nativeQuery = true
    )
    Long countByCategory(
        @Param("category") final String category,
        @Param("limit") final int limit
    );

    Page<Document> findAllByTitle_ValueContainingIgnoreCase(
        final String title,
        final Pageable pageable
    );

    List<Document> findAllByIdIn(final List<Long> ids);

    @Query(
        value = """
            SELECT *
            FROM document
            WHERE is_deleted = FALSE
            OFFSET floor(random() * (
                SELECT count(*) FROM document WHERE is_deleted = FALSE
            ))
            LIMIT 1
            """,
        nativeQuery = true
    )
    Document findRandomDocument();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM Document d WHERE d.id = :id")
    Optional<Document> findByIdForUpdate(@Param("id") final Long id);
}
