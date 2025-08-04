package com.openmpy.wiki.document.domain;

import com.openmpy.wiki.global.exception.CustomException;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DocumentContent {

    @Lob
    private String value;

    public DocumentContent(final String value) {
        validateBlank(value);

        this.value = value;
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException("내용이 빈 값일 수 없습니다.");
        }
    }
}
