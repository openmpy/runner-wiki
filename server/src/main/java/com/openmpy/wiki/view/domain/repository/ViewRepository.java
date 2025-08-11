package com.openmpy.wiki.view.domain.repository;

import com.openmpy.wiki.view.domain.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewRepository extends JpaRepository<View, String> {
}
