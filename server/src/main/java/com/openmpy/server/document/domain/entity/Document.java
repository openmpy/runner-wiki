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

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class Document {

    private static final long INITIAL_VERSION = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentCategory category;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<DocumentHistory> histories = new ArrayList<>();

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
        Document document = new Document(title, category);
        document.addHistory(author, content, INITIAL_VERSION, size, clientIp);
        return document;
    }

    public void addHistory(
            final String author,
            final String content,
            final Long version,
            final Long size,
            final String clientIp
    ) {
        final DocumentHistory history = DocumentHistory.create(author, content, version, size, clientIp);

        history.assignTo(this);
        histories.add(history);
    }

    public Long getMaximumVersion() {
        return histories.stream()
                .mapToLong(DocumentHistory::getVersion)
                .max()
                .orElse(0L);
    }

    public DocumentHistory getLastHistory() {
        return histories.getLast();
    }
}
