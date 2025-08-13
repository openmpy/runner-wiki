package com.openmpy.wiki.admin.domain.repository;

import com.openmpy.wiki.admin.domain.entity.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminLogRepository extends JpaRepository<AdminLog, String> {
}
