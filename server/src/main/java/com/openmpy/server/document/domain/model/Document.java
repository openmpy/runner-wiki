package com.openmpy.server.document.domain.model;

import com.openmpy.server.document.domain.type.DocumentCategory;
import com.openmpy.server.document.domain.vo.DocumentTitle;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
@Entity
public class Document extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "title", nullable = false))
    private DocumentTitle title;

    @Column
    private String titleChosung;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentCategory category;

    @Column(nullable = false)
    private Long latestVersion;

    @Column
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private final List<DocumentHistory> histories = new ArrayList<>();

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private final List<DocumentImage> images = new ArrayList<>();

    private Document(
            final String title,
            final DocumentCategory category
    ) {
        this.title = new DocumentTitle(title);
        this.titleChosung = KoreanChosung.toChosung(title);
        this.category = category;
        this.latestVersion = 0L;
    }

    public static Document create(final String title, final DocumentCategory category) {
        return new Document(title, category);
    }

    public void addHistory(
            final String author,
            final String content,
            final Long size,
            final String clientIp
    ) {
        if (deletedAt != null) {
            throw new CustomException("삭제된 문서는 수정할 수 없습니다.");
        }

        latestVersion++;

        final DocumentHistory history = DocumentHistory.create(author, content, latestVersion, size, clientIp);

        history.assignTo(this);
        histories.add(history);
    }

    public void attachImages(final List<DocumentImage> images) {
        if (deletedAt != null) {
            throw new CustomException("삭제된 문서에 이미지를 추가할 수 없습니다.");
        }

        for (final DocumentImage image : images) {
            image.markAsUsed(this);
            this.images.add(image);
        }
    }

    public void delete() {
        if (deletedAt != null) {
            return;
        }

        deletedAt = LocalDateTime.now();
        histories.forEach(DocumentHistory::delete);
        images.forEach(DocumentImage::delete);
    }

    public String getTitle() {
        return title.getValue();
    }
}
