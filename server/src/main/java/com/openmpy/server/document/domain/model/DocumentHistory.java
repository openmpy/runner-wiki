package com.openmpy.server.document.domain.model;

import com.openmpy.server.document.domain.DocumentHistoryAuthor;
import com.openmpy.server.document.domain.DocumentHistoryContent;
import com.openmpy.server.global.jpa.BaseTimeEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
@Entity
public class DocumentHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "author", nullable = false))
    private DocumentHistoryAuthor author;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(columnDefinition = "TEXT", name = "content", nullable = false))
    private DocumentHistoryContent content;

    @Column(nullable = false)
    private Long version;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private String clientIp;

    @Column
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    private DocumentHistory(
            final String author,
            final String content,
            final Long version,
            final Long size,
            final String clientIp
    ) {
        this.author = new DocumentHistoryAuthor(author);
        this.content = new DocumentHistoryContent(content);
        this.version = version;
        this.size = size;
        this.clientIp = clientIp;
    }

    protected static DocumentHistory create(
            final String author,
            final String content,
            final Long version,
            final Long size,
            final String clientIp
    ) {
        return new DocumentHistory(author, content, version, size, clientIp);
    }

    protected void assignTo(final Document document) {
        this.document = document;
    }

    public void delete() {
        if (deletedAt != null) {
            return;
        }

        deletedAt = LocalDateTime.now();
    }

    public String getAuthor() {
        return author.getValue();
    }

    public String getContent() {
        return content.getValue();
    }
}
