package com.openmpy.server.document.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.server.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DocumentHistorySizeTest {

    @DisplayName("문서 기록 크기 객체를 생성한다.")
    @Test
    void document_history_size_test_01() {
        // given & when
        final DocumentHistorySize size = new DocumentHistorySize(10L);

        // then
        assertThat(size.getValue()).isEqualTo(10L);
    }

    @DisplayName("값이 같으면 같은 객체이다.")
    @Test
    void document_history_size_test_02() {
        // given & when
        final DocumentHistorySize size1 = new DocumentHistorySize(10L);
        final DocumentHistorySize size2 = new DocumentHistorySize(10L);

        // then
        assertThat(size1).isEqualTo(size2);
    }

    @DisplayName("크기가 0 이하이면 예외가 발생한다.")
    @ParameterizedTest(name = "입력: {0}")
    @ValueSource(longs = {-1, 0})
    void exception_document_history_size_test_01(final long input) {
        // when & then
        assertThatThrownBy(() -> new DocumentHistorySize(input))
            .isInstanceOf(CustomException.class)
            .hasMessage("크기가 0 이하일 수 없습니다.");
    }
}