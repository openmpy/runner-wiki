package com.openmpy.server.document.domain.vo;

import com.openmpy.server.global.exception.CustomException;
import jakarta.persistence.Embeddable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DocumentImageUrl {

    private String value;

    public DocumentImageUrl(final String value) {
        validateBlank(value);
        validateUrl(value);

        this.value = value;
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException("URL이 빈 값일 수 없습니다.");
        }
    }

    private void validateUrl(final String value) {
        try {
            final URI uri = new URI(value);

            if (uri.getHost() == null) {
                throw new CustomException("올바른 URL이 아닙니다.");
            }
            if (!("http".equals(uri.getScheme()) || "https".equals(uri.getScheme()))) {
                throw new CustomException("http/https URL만 허용됩니다.");
            }
        } catch (final URISyntaxException e) {
            throw new CustomException("올바른 URL 형식이 아닙니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocumentImageUrl that = (DocumentImageUrl) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
