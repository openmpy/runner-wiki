package com.openmpy.server.document.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.server.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class DocumentTitleChosungTest {

    @DisplayName("문서 제목 초성 객체를 생성한다.")
    @Test
    void document_title_chosung_test_01() {
        // given & when
        final DocumentTitleChosung titleChosung = new DocumentTitleChosung("ㅈㅁ");

        // then
        assertThat(titleChosung.getValue()).isEqualTo("ㅈㅁ");
    }

    @DisplayName("값이 같으면 같은 객체이다.")
    @Test
    void document_title_chosung_test_02() {
        // given & when
        final DocumentTitleChosung titleChosung1 = new DocumentTitleChosung("ㅈㅁ");
        final DocumentTitleChosung titleChosung2 = new DocumentTitleChosung("ㅈㅁ");

        // then
        assertThat(titleChosung1).isEqualTo(titleChosung2);
    }

    @DisplayName("제목 초성이 빈 값이면 예외가 발생한다.")
    @ParameterizedTest(name = "입력: {0}")
    @NullAndEmptySource
    void exception_document_title_chosung_test_01(final String input) {
        // when & then
        assertThatThrownBy(() -> new DocumentTitleChosung(input))
            .isInstanceOf(CustomException.class)
            .hasMessageContaining("문서 초성 제목이 빈 값일 수 없습니다.");
    }

    @DisplayName("제목 초성이 빈 값이면 예외가 발생한다.")
    @Test
    void exception_document_title_chosung_test_02() {
        // given
        final String value = "제목";

        // when & then
        assertThatThrownBy(() -> new DocumentTitleChosung(value))
            .isInstanceOf(CustomException.class)
            .hasMessageContaining("문서 초성 제목이 올바르지 않습니다.");
    }
}