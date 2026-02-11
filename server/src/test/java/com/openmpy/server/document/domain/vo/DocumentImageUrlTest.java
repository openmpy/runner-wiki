package com.openmpy.server.document.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.server.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class DocumentImageUrlTest {

    @DisplayName("문서 이미지 URL 객체를 생성한다.")
    @ParameterizedTest(name = "입력: {0}")
    @ValueSource(strings = {"http://test.com/image", "https://test.com/image"})
    void document_image_url_test_01(final String input) {
        // when
        final DocumentImageUrl imageUrl = new DocumentImageUrl(input);

        // then
        assertThat(imageUrl.getValue()).isEqualTo(input);
    }

    @DisplayName("값이 같으면 같은 객체이다.")
    @Test
    void document_image_url_test_02() {
        // given & when
        final DocumentImageUrl imageUrl1 = new DocumentImageUrl("https://test.com/image");
        final DocumentImageUrl imageUrl2 = new DocumentImageUrl("https://test.com/image");

        // then
        assertThat(imageUrl1).isEqualTo(imageUrl2);
    }

    @DisplayName("URL이 빈 값이면 예외가 발생한다.")
    @ParameterizedTest(name = "입력: {0}")
    @NullAndEmptySource
    void exception_document_image_url_test_01(final String input) {
        // when & then
        assertThatThrownBy(() -> new DocumentImageUrl(input))
            .isInstanceOf(CustomException.class)
            .hasMessage("URL이 빈 값일 수 없습니다.");
    }

    @DisplayName("URL에 host가 없으면 예외가 발생한다.")
    @ParameterizedTest(name = "입력: {0}")
    @ValueSource(strings = {"http:/test.com", "https:///test.com", "https://:8080"})
    void exception_document_image_url_test_02(final String input) {
        // when & then
        assertThatThrownBy(() -> new DocumentImageUrl(input))
            .isInstanceOf(CustomException.class)
            .hasMessage("올바른 URL이 아닙니다.");
    }

    @DisplayName("URL이 http/https로 시작하지 않으면 예외가 발생한다.")
    @ParameterizedTest(name = "입력: {0}")
    @ValueSource(strings = {"ftp://example.com", "file://test.txt", "ws://example.com"})
    void exception_document_image_url_test_03(final String input) {
        // when & then
        assertThatThrownBy(() -> new DocumentImageUrl(input))
            .isInstanceOf(CustomException.class)
            .hasMessage("http/https URL만 허용됩니다.");
    }

    @DisplayName("URL이 올바른 형식이 아니면 예외가 발생한다.")
    @ParameterizedTest(name = "입력: {0}")
    @ValueSource(strings = {
        "https://exa mple.com",
        "https://test.com/<>",
        "https://%zz",
        "http://[::1"}
    )
    void exception_document_image_url_test_04(final String input) {
        // when & then
        assertThatThrownBy(() -> new DocumentImageUrl(input))
            .isInstanceOf(CustomException.class)
            .hasMessage("올바른 URL 형식이 아닙니다.");
    }
}