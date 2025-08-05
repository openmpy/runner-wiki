package com.openmpy.wiki.document.domain.entity;

import com.openmpy.wiki.document.domain.DocumentAuthor;
import com.openmpy.wiki.document.domain.DocumentContent;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "document_history")
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
    private boolean deleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    public static DocumentHistory create(
            final String id, final String author, final String content, final String clientIp, final Document document
    ) {
        final DocumentHistory documentHistory = new DocumentHistory();
        documentHistory.id = id;
        documentHistory.author = new DocumentAuthor(author);
        documentHistory.content = new DocumentContent(content);
        documentHistory.version = document.getHistory().size() + 1L;
        documentHistory.clientIp = clientIp;
        documentHistory.deleted = false;
        documentHistory.createdAt = LocalDateTime.now();
        documentHistory.document = document;
        return documentHistory;
    }

    public void delete() {
        deleted = true;
    }

    public String getAuthor() {
        return author.getValue();
    }

    public String getContent() {
        return content.getValue();
    }
}
