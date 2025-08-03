package com.openmpy.wiki.document.domain.repository;

import com.openmpy.wiki.document.domain.entity.Document;
import com.openmpy.wiki.document.domain.entity.DocumentHistory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long> {

    Optional<DocumentHistory> findFirstByDocumentAndDeletedFalseOrderByVersionDesc(final Document document);
}
