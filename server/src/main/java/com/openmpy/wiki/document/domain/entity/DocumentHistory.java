package com.openmpy.wiki.document.domain.entity;

import jakarta.persistence.Column;
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
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

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
            final Long id, final String author, final String content, final String clientIp, final Document document
    ) {
        final DocumentHistory documentHistory = new DocumentHistory();
        documentHistory.id = id;
        documentHistory.author = author;
        documentHistory.content = content;
        documentHistory.version = document.getHistory().size() + 1;
        documentHistory.clientIp = clientIp;
        documentHistory.deleted = false;
        documentHistory.createdAt = LocalDateTime.now();
        documentHistory.document = document;
        return documentHistory;
    }
}
