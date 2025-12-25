package com.openmpy.server.document.domain.repository;

import com.openmpy.server.document.domain.entity.DocumentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long> {
}
