package com.openmpy.wiki.image.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "image")
public class Image {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String clientIp;

    @Column
    private String documentId;

    @Column
    private boolean used;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime usedAt;

    public static Image create(final String id, final String key, final String clientIp) {
        final Image image = new Image();
        image.id = id;
        image.name = key;
        image.clientIp = clientIp;
        image.documentId = null;
        image.used = false;
        image.createdAt = LocalDateTime.now();
        image.usedAt = null;
        return image;
    }

    public void markUsed(final String documentId) {
        this.documentId = documentId;
        used = true;
        usedAt = LocalDateTime.now();
    }

    public void markDeleted() {
        used = false;
        usedAt = null;
    }
}
