package com.openmpy.wiki.view.domain.repository;

import com.openmpy.wiki.view.domain.entity.ViewCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewRepository extends JpaRepository<ViewCount, String> {
}
