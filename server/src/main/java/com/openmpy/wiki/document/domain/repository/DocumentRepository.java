package com.openmpy.wiki.document.domain.repository;

import com.openmpy.wiki.document.domain.constants.DocumentCategory;
import com.openmpy.wiki.document.domain.entity.Document;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentRepository extends JpaRepository<Document, String> {

    boolean existsByTitle_ValueAndCategory(final String title, final DocumentCategory category);

    @Query("SELECT d FROM Document d LEFT JOIN FETCH d.history WHERE d.id = :id")
    Optional<Document> findByIdWithHistory(@Param("id") final String id);

    @Query(
            value = "SELECT d.id, d.title, d.category, d.status, d.created_at, d.updated_at " +
                    "FROM (" +
                    "  SELECT id FROM document " +
                    "  ORDER BY updated_at DESC " +
                    "  LIMIT :limit OFFSET :offset" +
                    ") t LEFT JOIN document d ON t.id = d.id",
            nativeQuery = true
    )
    List<Document> findAllOrderByUpdatedAtDesc(
            @Param("offset") final int offset,
            @Param("limit") final int limit
    );

    @Query(
            value = "SELECT count(*) " +
                    "FROM (" +
                    "  SELECT id FROM document " +
                    "  LIMIT :limit" +
                    ") t",
            nativeQuery = true
    )
    Long count(@Param("limit") final int limit);

    List<Document> findByIdIn(final Collection<String> ids);
}
