package com.openmpy.server.document.domain.entity;

import com.openmpy.server.document.domain.constants.DocumentCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentCategory category;

    @Column(nullable = false)
    private Long latestVersion;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

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
        this.title = title;
        this.category = category;
        this.latestVersion = 0L;
        this.createdAt = LocalDateTime.now();
    }

    public static Document create(
            final String title,
            final DocumentCategory category,
            final String author,
            final String content,
            final Long size,
            final String clientIp
    ) {
        final Document document = new Document(title, category);

        document.addHistory(author, content, size, clientIp);
        return document;
    }

    public void addHistory(
            final String author,
            final String content,
            final Long size,
            final String clientIp
    ) {
        if (deletedAt != null) {
            throw new IllegalArgumentException("삭제된 문서는 수정할 수 없습니다.");
        }

        latestVersion++;

        final DocumentHistory history = DocumentHistory.create(author, content, latestVersion, size, clientIp);

        history.assignTo(this);
        histories.add(history);

        updatedAt = LocalDateTime.now();
    }

    public void attachImages(final List<DocumentImage> images) {
        if (deletedAt != null) {
            throw new IllegalArgumentException("삭제된 문서에 이미지를 추가할 수 없습니다.");
        }

        for (final DocumentImage image : images) {
            image.markAsUsed(this);
            this.images.add(image);
        }

        updatedAt = LocalDateTime.now();
    }

    public void delete() {
        if (deletedAt != null) {
            return;
        }

        deletedAt = LocalDateTime.now();
        updatedAt = deletedAt;

        histories.forEach(DocumentHistory::delete);
        images.forEach(DocumentImage::delete);
    }

    public DocumentHistory getLastHistory() {
        return histories.stream()
                .max(Comparator.comparing(DocumentHistory::getVersion))
                .orElseThrow(() -> new IllegalStateException("문서 히스토리가 없습니다."));
    }
}
