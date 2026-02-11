package com.openmpy.server.document.domain.entity;

import com.openmpy.server.document.domain.type.DocumentCategory;
import com.openmpy.server.document.domain.vo.DocumentTitle;
import com.openmpy.server.document.domain.vo.DocumentTitleChosung;
import com.openmpy.server.global.exception.CustomException;
import com.openmpy.server.global.jpa.BaseTimeEntity;
import com.openmpy.server.global.util.KoreanChosung;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@SQLRestriction("is_deleted = FALSE")
@Entity
public class Document extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "title", nullable = false))
    private DocumentTitle title;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "title_chosung", nullable = false))
    private DocumentTitleChosung titleChosung;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private DocumentCategory category;

    @Column(name = "latest_version", nullable = false)
    private Integer latestVersion;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private final List<DocumentHistory> histories = new ArrayList<>();

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private final List<DocumentImage> images = new ArrayList<>();

    public static Document create(final String title, final DocumentCategory category) {
        final String chosung = KoreanChosung.extract(title);

        return Document.builder()
            .title(new DocumentTitle(title))
            .titleChosung(new DocumentTitleChosung(chosung))
            .category(category)
            .latestVersion(0)
            .isDeleted(false)
            .build();
    }

    public void addHistory(
        final String author,
        final String content,
        final Long size,
        final String clientIp
    ) {
        if (Boolean.TRUE.equals(isDeleted)) {
            throw new CustomException("삭제된 문서에 추가할 수 없습니다.");
        }

        latestVersion++;

        final DocumentHistory history = DocumentHistory.create(
            author,
            content,
            latestVersion,
            size,
            clientIp
        );

        history.assignTo(this);
        histories.add(history);
    }

    public void attachImages(final List<DocumentImage> images) {
        if (Boolean.TRUE.equals(isDeleted)) {
            throw new CustomException("삭제된 문서에 이미지를 추가할 수 없습니다.");
        }

        for (final DocumentImage image : images) {
            if (image == null) {
                throw new CustomException("이미지가 null 값입니다.");
            }

            image.markAsUsed(this);
            this.images.add(image);
        }
    }

    public void delete() {
        if (Boolean.TRUE.equals(isDeleted)) {
            throw new CustomException("이미 삭제된 문서입니다.");
        }

        isDeleted = true;
        deletedAt = LocalDateTime.now();

        histories.forEach(DocumentHistory::delete);
        images.forEach(DocumentImage::delete);
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getTitleChosung() {
        return titleChosung.getValue();
    }
}
