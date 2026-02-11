package com.openmpy.server.document.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.server.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class DocumentHistoryContentTest {

    @DisplayName("문서 기록 내용 객체를 생성한다.")
    @Test
    void document_history_content_test_01() {
        // given & when
        final DocumentHistoryContent content = new DocumentHistoryContent("내용");

        // then
        assertThat(content.getValue()).isEqualTo("내용");
    }

    @DisplayName("값이 같으면 같은 객체이다.")
    @Test
    void document_history_content_test_02() {
        // given & when
        final DocumentHistoryContent content1 = new DocumentHistoryContent("내용");
        final DocumentHistoryContent content2 = new DocumentHistoryContent("내용");

        // then
        assertThat(content1).isEqualTo(content2);
    }

    @DisplayName("문서 기록 내용이 빈 값이면 예외가 발생한다.")
    @ParameterizedTest(name = "입력: {0}")
    @NullAndEmptySource
    void exception_document_history_content_test_01(final String input) {
        // when & then
        assertThatThrownBy(() -> new DocumentHistoryContent(input))
            .isInstanceOf(CustomException.class)
            .hasMessage("내용이 빈 값일 수 없습니다.");
    }
}