package com.openmpy.server.document.domain.entity;

import com.openmpy.server.document.domain.type.DocumentImageStatus;
import com.openmpy.server.document.domain.vo.DocumentImageUrl;
import com.openmpy.server.global.exception.CustomException;
import com.openmpy.server.global.jpa.BaseTimeEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
@Entity
public class DocumentImage extends BaseTimeEntity {

    private static final int EXPIRATION_HOURS = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "url", nullable = false))
    private DocumentImageUrl url;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DocumentImageStatus status;

    @Column(name = "client_ip", nullable = false)
    private String clientIp;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    public static DocumentImage create(final String url, final String clientIp) {
        return DocumentImage.builder()
            .url(new DocumentImageUrl(url))
            .status(DocumentImageStatus.TEMP)
            .clientIp(clientIp)
            .expiredAt(LocalDateTime.now().plusHours(EXPIRATION_HOURS))
            .build();
    }

    protected void markAsUsed(final Document document) {
        if (document == null) {
            throw new CustomException("문서가 null 값입니다.");
        }
        if (status != DocumentImageStatus.TEMP) {
            throw new CustomException("TEMP 이미지만 사용할 수 있습니다.");
        }
        if (expiredAt != null && LocalDateTime.now().isAfter(expiredAt)) {
            throw new CustomException("만료된 이미지입니다.");
        }
        if (deletedAt != null) {
            throw new CustomException("이미 삭제된 이미지입니다.");
        }

        this.document = document;
        status = DocumentImageStatus.USED;
        expiredAt = null;
    }

    public void delete() {
        if (deletedAt != null) {
            throw new CustomException("이미 삭제된 문서 이미지입니다.");
        }

        deletedAt = LocalDateTime.now();
    }

    public String getUrl() {
        return url.getValue();
    }
}
