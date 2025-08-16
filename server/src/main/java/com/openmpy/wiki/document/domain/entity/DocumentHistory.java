package com.openmpy.wiki.document.domain.entity;

import com.openmpy.wiki.document.domain.DocumentAuthor;
import com.openmpy.wiki.document.domain.DocumentContent;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "document_history", indexes = {
        @Index(name = "idx_document_history_document_id", columnList = "document_id"),
        @Index(name = "idx_document_history_documentId_deleted_version_desc", columnList = "document_id, deleted, version DESC")
})
public class DocumentHistory {

    @Id
    private String id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "author", nullable = false))
    private DocumentAuthor author;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "content", columnDefinition = "LONGTEXT", nullable = false))
    private DocumentContent content;

    @Column
    private long version = 0L;

    @Column(nullable = false)
    private String clientIp;

    @Column
    private Double score;

    @Column
    private boolean deleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    public static DocumentHistory create(
            final String id,
            final String author,
            final String content,
            final String clientIp,
            final long version,
            final Document document
    ) {
        final DocumentHistory documentHistory = new DocumentHistory();
        documentHistory.id = id;
        documentHistory.author = new DocumentAuthor(author);
        documentHistory.content = new DocumentContent(content);
        documentHistory.version = version;
        documentHistory.clientIp = clientIp;
        documentHistory.deleted = false;
        documentHistory.createdAt = LocalDateTime.now();
        documentHistory.document = document;
        return documentHistory;
    }

    public void delete() {
        deleted = true;
    }

    public void recover() {
        deleted = false;
    }

    public void updateScore(final Double score) {
        this.score = score;
    }

    public String getAuthor() {
        return author.getValue();
    }

    public String getContent() {
        return content.getValue();
    }
}
