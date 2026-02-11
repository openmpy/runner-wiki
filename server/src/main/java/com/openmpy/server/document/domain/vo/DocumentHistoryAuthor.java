package com.openmpy.server.document.domain.vo;

import com.openmpy.server.global.exception.CustomException;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DocumentHistoryAuthor {

    private static final int AUTHOR_MAX_LENGTH = 10;

    private String value;

    public DocumentHistoryAuthor(final String value) {
        validateBlank(value);
        validateLength(value);

        this.value = value;
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException("작성자가 빈 값일 수 없습니다.");
        }
    }

    private void validateLength(final String value) {
        if (value.length() > AUTHOR_MAX_LENGTH) {
            throw new CustomException("작성자 길이가 10자를 넘길 수 없습니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocumentHistoryAuthor author = (DocumentHistoryAuthor) o;
        return Objects.equals(value, author.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
