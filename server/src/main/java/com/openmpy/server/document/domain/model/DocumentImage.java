package com.openmpy.server.document.domain.model;

import com.openmpy.server.document.domain.type.DocumentImageStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class DocumentImage {

    private static final int EXPIRATION_HOURS = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentImageStatus status;

    @Column(nullable = false)
    private String clientIp;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime expiredAt;

    @Column
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    private DocumentImage(final String url, final String clientIp) {
        this.url = url;
        this.status = DocumentImageStatus.TEMP;
        this.clientIp = clientIp;
        this.createdAt = LocalDateTime.now();
        this.expiredAt = LocalDateTime.now().plusHours(EXPIRATION_HOURS);
    }

    public static DocumentImage create(final String url, final String clientIp) {
        return new DocumentImage(url, clientIp);
    }

    protected void markAsUsed(final Document document) {
        if (deletedAt != null) {
            throw new IllegalArgumentException("이미 삭제된 이미지입니다.");
        }
        if (status != DocumentImageStatus.TEMP) {
            throw new IllegalArgumentException("TEMP 이미지만 사용할 수 있습니다.");
        }
        if (expiredAt != null && expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("만료된 이미지입니다.");
        }

        this.document = document;

        status = DocumentImageStatus.USED;
        expiredAt = null;
    }

    public void delete() {
        if (deletedAt != null) {
            return;
        }

        deletedAt = LocalDateTime.now();
    }
}
