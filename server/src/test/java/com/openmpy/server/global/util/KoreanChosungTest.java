package com.openmpy.server.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class KoreanChosungTest {

    @DisplayName("입력 값에서 초성을 추출한다.")
    @Test
    void korean_chosung_test_01() {
        // given
        final String value = "가나다";

        // when
        final String chosung = KoreanChosung.extract(value);

        // then
        assertThat(chosung).isEqualTo("ㄱㄴㄷ");
    }

    @DisplayName("입력 값이 null 또는 빈 값일 경우 빈 값을 반환한다.")
    @ParameterizedTest(name = "입력: {0}")
    @NullAndEmptySource
    void korean_chosung_test_02(final String input) {
        // when
        final String chosung = KoreanChosung.extract(input);

        // then
        assertThat(chosung).isEmpty();
    }
}