package com.openmpy.server.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ContentCalculatorTest {

    @DisplayName("입력 값의 크기를 byte로 반환한다.")
    @Test
    void content_calculator_test_01() {
        // given & when
        final long size = ContentCalculator.calculateUtf8Bytes("가나다");

        // then
        assertThat(size).isEqualTo(9);
    }

    @DisplayName("입력 값이 null 또는 빈 값일 경우 0을 반환한다.")
    @ParameterizedTest(name = "입력: {0}")
    @NullAndEmptySource
    void content_calculator_test_02(final String input) {
        // when
        final long size = ContentCalculator.calculateUtf8Bytes(input);

        // then
        assertThat(size).isZero();
    }
}