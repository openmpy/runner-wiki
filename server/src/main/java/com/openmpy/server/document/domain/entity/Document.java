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
    private Long latestVersion = 0L;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<DocumentHistory> histories = new ArrayList<>();

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<DocumentImage> images = new ArrayList<>();

    private Document(
            final String title,
            final DocumentCategory category
    ) {
        this.title = title;
        this.category = category;
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
        document.updatedAt = LocalDateTime.now();
        return document;
    }

    public void addHistory(
            final String author,
            final String content,
            final Long size,
            final String clientIp
    ) {
        latestVersion++;

        final DocumentHistory history = DocumentHistory.create(author, content, latestVersion, size, clientIp);

        history.assignTo(this);
        histories.add(history);

        updatedAt = LocalDateTime.now();
    }

    public void assignImage(final DocumentImage image) {
        image.assignTo(this);
        images.add(image);
    }

    public void delete() {
        deletedAt = LocalDateTime.now();
    }

    public DocumentHistory getLastHistory() {
        return histories.getLast();
    }
}
