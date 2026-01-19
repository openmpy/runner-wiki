package com.openmpy.server.document.domain.repository;

import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.type.DocumentCategory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    boolean existsByTitle_ValueAndCategory(final String title, final DocumentCategory category);

    @Query(
            value = "SELECT "
                    + "d.id, d.title, d.title_chosung, d.category, d.latest_version, "
                    + "d.created_at, d.updated_at, d.deleted_at "
                    + "FROM ( "
                    + "  SELECT id FROM document "
                    + "  WHERE deleted_at IS NULL "
                    + "  ORDER BY updated_at DESC "
                    + "  LIMIT :limit OFFSET :offset "
                    + ") t "
                    + "LEFT JOIN document d ON t.id = d.id "
                    + "WHERE d.deleted_at IS NULL",
            nativeQuery = true
    )
    List<Document> findAllOrderByUpdatedAtDesc(
            @Param("offset") final int offset,
            @Param("limit") final int limit
    );

    @Query(
            value = "SELECT "
                    + "d.id, d.title, d.title_chosung, d.category, d.latest_version, "
                    + "d.created_at, d.updated_at, d.deleted_at "
                    + "FROM ( "
                    + "  SELECT id FROM document "
                    + "  WHERE category = :category "
                    + "    AND deleted_at IS NULL "
                    + "  ORDER BY updated_at DESC "
                    + "  LIMIT :limit OFFSET :offset "
                    + ") t "
                    + "LEFT JOIN document d ON t.id = d.id "
                    + "WHERE d.deleted_at IS NULL",
            nativeQuery = true
    )
    List<Document> findAllByCategoryOrderByUpdatedAtDesc(
            @Param("category") final String category,
            @Param("offset") final int offset,
            @Param("limit") final int limit
    );

    @Query(
            value = "SELECT count(*) " +
                    "FROM ( " +
                    "  SELECT id FROM document " +
                    "  WHERE deleted_at IS NULL " +
                    "  LIMIT :limit " +
                    ") t",
            nativeQuery = true
    )
    Long count(
            @Param("limit") final int limit
    );

    @Query(
            value = "SELECT count(*) " +
                    "FROM ( " +
                    "  SELECT id FROM document " +
                    "  WHERE category = :category " +
                    "    AND deleted_at IS NULL " +
                    "  LIMIT :limit " +
                    ") t",
            nativeQuery = true
    )
    Long countByCategory(
            @Param("category") final String category,
            @Param("limit") final int limit
    );

    Page<Document> findAllByTitle_ValueContainingIgnoreCase(final String title, final Pageable pageable);

    List<Document> findAllByIdIn(final List<Long> ids);

    @Query(
            value = """
                      SELECT *
                      FROM document
                      WHERE id >= (
                        SELECT floor(random() * (SELECT max(id) FROM document)) + 1
                      )
                      ORDER BY id
                      LIMIT 1
                    """,
            nativeQuery = true
    )
    Document findRandomDocument();
}
