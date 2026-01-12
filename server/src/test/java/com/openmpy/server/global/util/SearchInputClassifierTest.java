package com.openmpy.server.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SearchInputClassifierTest {

    @Test
    void 초성이_포함되어_있을_경우_True_반환() {
        // given
        final String input = "ㅈㅁ";

        // when
        final boolean result = SearchInputClassifier.isChosungQuery(input);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 초성이_포함되어_있지_않을_경우_False_반환() {
        // given
        final String input = "테스트";

        // when
        final boolean result = SearchInputClassifier.isChosungQuery(input);

        // then
        assertThat(result).isFalse();
    }
}