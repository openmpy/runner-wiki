package com.openmpy.server.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class KoreanChosungTest {

    @Test
    void 한글에서_초성을_추출한다() {
        // given
        final String input = "대한민국";

        // when
        final String chosung = KoreanChosung.toChosung(input);

        // then
        assertThat(chosung).isEqualTo("ㄷㅎㅁㄱ");
    }

    @Test
    void 영어_숫자_특수문자가_포함된_한글에서_초성을_추출한다() {
        // given
        final String input = "대한민국aBc123!@#";

        // when
        final String chosung = KoreanChosung.toChosung(input);

        // then
        assertThat(chosung).isEqualTo("ㄷㅎㅁㄱaBc123!@#");
    }
}