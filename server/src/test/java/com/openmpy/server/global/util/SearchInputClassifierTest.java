package com.openmpy.server.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class SearchInputClassifierTest {

    @DisplayName("입력 값이 초성일 경우 True를 반환한다.")
    @Test
    void search_input_classifier_test_01() {
        // given
        final String value = "ㄱㄴㄷ";

        // when & then
        assertThat(SearchInputClassifier.isChosungQuery(value)).isTrue();
    }

    @DisplayName("입력 값이 null 또는 빈 값일 경우 False를 반환한다.")
    @ParameterizedTest(name = "입력: {0}")
    @NullAndEmptySource
    void search_input_classifier_test_02(final String input) {
        // when & then
        assertThat(SearchInputClassifier.isChosungQuery(input)).isFalse();
    }

    @DisplayName("입력 값에 초성이 아닌 값이 포함되어 있는 경우 False를 반환한다.")
    @Test
    void search_input_classifier_test_03() {
        // given
        final String value = "ㄱ나ㄷ";

        // when & then
        assertThat(SearchInputClassifier.isChosungQuery(value)).isFalse();
    }
}