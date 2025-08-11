package com.openmpy.wiki.view.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "view")
public class ViewCount {

    @Id
    private String documentId;

    @Column
    private long count = 0L;

    public static ViewCount init(final String documentId) {
        final ViewCount viewCount = new ViewCount();
        viewCount.documentId = documentId;
        viewCount.count = 0L;
        return viewCount;
    }

    public void increment(final long count) {
        this.count += count;
    }
}
