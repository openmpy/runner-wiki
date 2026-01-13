package com.openmpy.server.document.domain.vo;

import com.openmpy.server.global.exception.CustomException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DocumentHistoryContent {

    private String value;

    public DocumentHistoryContent(final String value) {
        validateBlank(value);

        this.value = value;
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException("내용이 빈 값일 수 없습니다.");
        }
    }
}
