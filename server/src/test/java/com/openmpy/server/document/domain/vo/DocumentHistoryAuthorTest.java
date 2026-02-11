package com.openmpy.server.document.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.server.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class DocumentHistoryAuthorTest {

    @DisplayName("문서 기록 작성자 객체를 생성한다.")
    @Test
    void document_history_author_test_01() {
        // given & when
        final DocumentHistoryAuthor author = new DocumentHistoryAuthor("작성자");

        // then
        assertThat(author.getValue()).isEqualTo("작성자");
    }

    @DisplayName("값이 같으면 같은 객체이다.")
    @Test
    void document_history_author_test_02() {
        // given & when
        final DocumentHistoryAuthor author1 = new DocumentHistoryAuthor("작성자");
        final DocumentHistoryAuthor author2 = new DocumentHistoryAuthor("작성자");

        // then
        assertThat(author1).isEqualTo(author2);
    }

    @DisplayName("작성자가 빈 값이면 예외가 발생한다.")
    @ParameterizedTest(name = "입력: {0}")
    @NullAndEmptySource
    void exception_document_history_author_test_01(final String input) {
        // when & then
        assertThatThrownBy(() -> new DocumentHistoryAuthor(input))
            .isInstanceOf(CustomException.class)
            .hasMessage("작성자가 빈 값일 수 없습니다.");
    }

    @DisplayName("작성자 길이가 10자를 넘어가면 예외가 발생한다.")
    @Test
    void exception_document_history_author_test_02() {
        // given
        final String value = "1".repeat(11);

        // when & then
        assertThatThrownBy(() -> new DocumentHistoryAuthor(value))
            .isInstanceOf(CustomException.class)
            .hasMessage("작성자 길이가 10자를 넘길 수 없습니다.");
    }
}