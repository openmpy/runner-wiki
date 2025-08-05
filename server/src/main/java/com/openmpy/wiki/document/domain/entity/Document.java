package com.openmpy.wiki.document.domain.entity;

import com.openmpy.wiki.document.domain.DocumentTitle;
import com.openmpy.wiki.document.domain.constants.DocumentCategory;
import com.openmpy.wiki.document.domain.constants.DocumentStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "document")
public class Document {

    @Id
    private String id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "title", nullable = false))
    private DocumentTitle title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentHistory> history = new ArrayList<>();

    public static Document create(final String id, final String title, final DocumentCategory category) {
        final Document document = new Document();
        document.id = id;
        document.title = new DocumentTitle(title);
        document.category = category;
        document.status = DocumentStatus.ACTIVE;
        document.createdAt = document.updatedAt = LocalDateTime.now();
        return document;
    }

    public void addHistory(final DocumentHistory documentHistory) {
        history.add(documentHistory);
        updatedAt = LocalDateTime.now();
    }

    public String getTitle() {
        return title.getValue();
    }
}
