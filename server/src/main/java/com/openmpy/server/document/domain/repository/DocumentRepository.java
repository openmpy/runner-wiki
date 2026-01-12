package com.openmpy.server.document.domain.repository;

import com.openmpy.server.document.domain.model.Document;
import com.openmpy.server.document.domain.type.DocumentCategory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    boolean existsByTitle_ValueAndCategory(final String title, final DocumentCategory category);

    Page<Document> findAllByCategory(final DocumentCategory category, final Pageable pageable);

    Page<Document> findAllByTitle_ValueContainingIgnoreCase(final String title, final Pageable pageable);

    Page<Document> findByTitleChosungIsNullOrTitleChosungEquals(final String titleChosung, final Pageable pageable);

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
