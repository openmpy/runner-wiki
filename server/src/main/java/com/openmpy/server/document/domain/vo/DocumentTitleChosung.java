package com.openmpy.server.document.domain.vo;

import com.openmpy.server.global.exception.CustomException;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DocumentTitleChosung {

    private static final String TITLE_CHOSUNG_VALIDATION_REGEX = "^[a-zA-Z0-9ㄱ-ㅎ]+$";

    private String value;

    public DocumentTitleChosung(final String value) {
        validateBlank(value);
        validateTitleChosung(value);

        this.value = value;
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException("문서 초성 제목이 빈 값일 수 없습니다.");
        }
    }

    private void validateTitleChosung(final String value) {
        final Pattern pattern = Pattern.compile(TITLE_CHOSUNG_VALIDATION_REGEX);

        if (!pattern.matcher(value).matches()) {
            throw new CustomException("문서 초성 제목이 올바르지 않습니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocumentTitleChosung that = (DocumentTitleChosung) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
