package com.openmpy.server.document.domain.repository.search;

import com.openmpy.server.document.domain.entity.Document;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DocumentSearchRepositoryImpl implements DocumentSearchRepository {

    private final EntityManager em;

    @Override
    public Page<Document> searchByChosungPrefix(final String query, final Pageable pageable) {
        final List<Document> content = em.createNativeQuery("""
                    SELECT *
                    FROM document
                    WHERE deleted_at IS NULL
                      AND title_chosung LIKE :q || '%'
                    ORDER BY id DESC
                    LIMIT :limit OFFSET :offset
                """, Document.class)
            .setParameter("q", query)
            .setParameter("limit", pageable.getPageSize())
            .setParameter("offset", (int) pageable.getOffset())
            .getResultList();

        final long total = ((Number) em.createNativeQuery("""
                    SELECT COUNT(*)
                    FROM document
                    WHERE deleted_at IS NULL
                      AND title_chosung LIKE :q || '%'
                """)
            .setParameter("q", query).getSingleResult())
            .longValue();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Document> searchByTitlePrefix(final String query, final Pageable pageable) {
        final List<Document> content = em.createNativeQuery("""
                    SELECT *
                    FROM document
                    WHERE deleted_at IS NULL
                      AND title_norm LIKE lower(:q) || '%'
                    ORDER BY id DESC
                    LIMIT :limit OFFSET :offset
                """, Document.class)
            .setParameter("q", query)
            .setParameter("limit", pageable.getPageSize())
            .setParameter("offset", (int) pageable.getOffset())
            .getResultList();

        final long total = ((Number) em.createNativeQuery("""
                    SELECT COUNT(*)
                    FROM document
                    WHERE deleted_at IS NULL
                      AND title_norm LIKE lower(:q) || '%'
                """)
            .setParameter("q", query).getSingleResult())
            .longValue();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Document> searchByTrgm(final String query, final Pageable pageable) {
        final List<Document> content = em.createNativeQuery("""
                    SELECT *
                    FROM document
                    WHERE deleted_at IS NULL
                      AND title ILIKE '%' || :q || '%'
                    ORDER BY similarity(title, :q) DESC
                    LIMIT :limit OFFSET :offset
                """, Document.class)
            .setParameter("q", query)
            .setParameter("limit", pageable.getPageSize())
            .setParameter("offset", (int) pageable.getOffset())
            .getResultList();

        final long total = ((Number) em.createNativeQuery("""
                    SELECT COUNT(*)
                    FROM document
                    WHERE deleted_at IS NULL
                      AND title ILIKE '%' || :q || '%'
                """)
            .setParameter("q", query).getSingleResult())
            .longValue();

        return new PageImpl<>(content, pageable, total);
    }
}
