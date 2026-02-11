package com.openmpy.server.document.domain.entity;

import com.openmpy.server.document.domain.vo.DocumentHistoryAuthor;
import com.openmpy.server.document.domain.vo.DocumentHistoryContent;
import com.openmpy.server.document.domain.vo.DocumentHistorySize;
import com.openmpy.server.document.domain.vo.DocumentHistoryVersion;
import com.openmpy.server.global.exception.CustomException;
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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
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
@SQLRestriction("deleted_at IS NULL")
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_document_history_document_version",
            columnNames = {"document_id", "version"}
        )
    }
)
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

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "version", nullable = false))
    private DocumentHistoryVersion version;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "size", nullable = false))
    private DocumentHistorySize size;

    @Column(name = "client_ip", nullable = false)
    private String clientIp;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    public static DocumentHistory create(
        final String author,
        final String content,
        final Integer version,
        final Long size,
        final String clientIp
    ) {
        return DocumentHistory.builder()
            .author(new DocumentHistoryAuthor(author))
            .content(new DocumentHistoryContent(content))
            .version(new DocumentHistoryVersion(version))
            .size(new DocumentHistorySize(size))
            .clientIp(clientIp)
            .build();
    }

    protected void assignTo(final Document document) {
        if (document == null) {
            throw new CustomException("문서가 null 값입니다.");
        }

        this.document = document;
    }

    public void delete() {
        if (deletedAt != null) {
            throw new CustomException("이미 삭제된 문서 기록입니다.");
        }

        deletedAt = LocalDateTime.now();
    }

    public String getAuthor() {
        return author.getValue();
    }

    public String getContent() {
        return content.getValue();
    }

    public Integer getVersion() {
        return version.getValue();
    }

    public Long getSize() {
        return size.getValue();
    }
}
