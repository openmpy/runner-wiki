package com.openmpy.server.document.domain.repository;

import com.openmpy.server.document.domain.model.DocumentImage;
import com.openmpy.server.document.domain.type.DocumentImageStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentImageRepository extends JpaRepository<DocumentImage, Long> {

    List<DocumentImage> findAllByIdInAndStatus(final List<Long> ids, final DocumentImageStatus status);

    List<DocumentImage> findAllByStatusAndExpiredAtBefore(
            final DocumentImageStatus status, final LocalDateTime localDateTime
    );
}
