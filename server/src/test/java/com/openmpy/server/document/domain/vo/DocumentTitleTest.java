package com.openmpy.server.document.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.server.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class DocumentTitleTest {

    @DisplayName("문서 제목 객체를 생성한다.")
    @Test
    void document_title_test_01() {
        // given & when
        final DocumentTitle title = new DocumentTitle("제목");

        // then
        assertThat(title.getValue()).isEqualTo("제목");
    }

    @DisplayName("값이 같으면 같은 객체이다.")
    @Test
    void document_title_test_02() {
        // given & when
        final DocumentTitle title1 = new DocumentTitle("제목");
        final DocumentTitle title2 = new DocumentTitle("제목");

        // then
        assertThat(title1).isEqualTo(title2);
    }

    @DisplayName("문서 제목이 빈 값이면 예외가 발생한다.")
    @ParameterizedTest(name = "입력 = {0}")
    @NullAndEmptySource
    void exception_document_title_test_01(final String input) {
        // when & then
        assertThatThrownBy(() -> new DocumentTitle(input))
            .isInstanceOf(CustomException.class)
            .hasMessage("문서 제목이 빈 값일 수 없습니다.");
    }

    @DisplayName("문서 제목 길이가 10자를 넘어갈 경우 예외가 발생한다.")
    @Test
    void exception_document_title_test_02() {
        // given
        final String value = "1".repeat(11);

        // when & then
        assertThatThrownBy(() -> new DocumentTitle(value))
            .isInstanceOf(CustomException.class)
            .hasMessage("문서 제목 길이가 10자를 넘길 수 없습니다.");
    }

    @DisplayName("문서 제목에 공백, 특수문자가 들어갈 경우 예외가 발생한다.")
    @ParameterizedTest(name = "입력 = {0}")
    @ValueSource(strings = {" 제목", "제목 ", "제!목"})
    void exception_document_title_test_03(final String input) {
        // when & then
        assertThatThrownBy(() -> new DocumentTitle(input))
            .isInstanceOf(CustomException.class)
            .hasMessage("문서 제목이 올바르지 않습니다.");
    }
}