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
public class DocumentHistoryVersion {

    private Integer value;

    public DocumentHistoryVersion(final Integer value) {
        validate(value);

        this.value = value;
    }

    private void validate(final Integer value) {
        if (value <= 0) {
            throw new CustomException("버전이 0 이하일 수 없습니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocumentHistoryVersion that = (DocumentHistoryVersion) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
