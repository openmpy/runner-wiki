package com.openmpy.wiki.image.domain.repository;

import com.openmpy.wiki.image.domain.entity.Image;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, String> {

    List<Image> findAllByDocumentId(final String documentId);

    List<Image> findAllByUsedFalseAndCreatedAtBefore(final LocalDateTime cutoffTime);
}
