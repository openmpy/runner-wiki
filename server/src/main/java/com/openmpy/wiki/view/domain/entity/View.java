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
public class View {

    @Id
    private String documentId;

    @Column
    private long count = 0L;

    public static View init(final String documentId) {
        final View view = new View();
        view.documentId = documentId;
        view.count = 0L;
        return view;
    }

    public void increment(final long count) {
        this.count += count;
    }
}
