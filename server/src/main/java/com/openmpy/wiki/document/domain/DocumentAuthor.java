package com.openmpy.wiki.document.domain;

import com.openmpy.wiki.global.exception.CustomException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DocumentAuthor {

    private String value;

    public DocumentAuthor(final String value) {
        validateBlank(value);
        validateLength(value);

        this.value = value;
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException("작성자명이 빈 값일 수 없습니다.");
        }
    }

    private void validateLength(final String value) {
        if (value.length() > 10) {
            throw new CustomException("작성자명은 최대 10자까지 가능합니다.");
        }
    }
}
