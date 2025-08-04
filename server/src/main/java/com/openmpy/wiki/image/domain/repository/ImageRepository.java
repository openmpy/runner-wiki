package com.openmpy.wiki.image.domain.repository;

import com.openmpy.wiki.image.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
