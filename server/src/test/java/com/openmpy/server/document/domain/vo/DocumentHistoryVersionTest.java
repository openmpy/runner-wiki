package com.openmpy.server.document.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.openmpy.server.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DocumentHistoryVersionTest {

    @DisplayName("문서 기록 버전 객체를 생성한다.")
    @Test
    void document_history_version_test_01() {
        // given & when
        final DocumentHistoryVersion version = new DocumentHistoryVersion(1);

        // then
        assertThat(version.getValue()).isEqualTo(1);
    }

    @DisplayName("값이 같으면 같은 객체이다.")
    @Test
    void document_history_version_test_02() {
        // given & when
        final DocumentHistoryVersion version1 = new DocumentHistoryVersion(1);
        final DocumentHistoryVersion version2 = new DocumentHistoryVersion(1);

        // then
        assertThat(version1).isEqualTo(version2);
    }

    @DisplayName("버전이 0 이하이면 예외가 발생한다.")
    @ParameterizedTest(name = "입력: {0}")
    @ValueSource(ints = {-1, 0})
    void exception_document_history_version_test_01(final int input) {
        // when & then
        assertThatThrownBy(() -> new DocumentHistoryVersion(input))
            .isInstanceOf(CustomException.class)
            .hasMessage("버전이 0 이하일 수 없습니다.");
    }
}