package com.openmpy.wiki.document.domain;

import com.openmpy.wiki.global.exception.CustomException;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DocumentTitle {

    private static final String TITLE_VALIDATION_REGEX = "^[a-zA-Z0-9가-힣]+$";

    private String value;

    public DocumentTitle(final String value) {
        validateBlank(value);
        validateLength(value);
        validateTitle(value);

        this.value = value;
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException("제목이 빈 값일 수 없습니다.");
        }
    }

    private void validateLength(final String value) {
        if (value.length() > 10) {
            throw new CustomException("제목은 최대 10자까지 가능합니다.");
        }
    }

    private void validateTitle(final String value) {
        final Pattern pattern = Pattern.compile(TITLE_VALIDATION_REGEX);
        if (!pattern.matcher(value).matches()) {
            throw new CustomException("제목이 올바르지 않습니다.");
        }
    }
}
