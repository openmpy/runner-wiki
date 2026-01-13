package com.openmpy.server.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.openmpy.server.document.application.support.SearchInputClassifier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class SearchInputClassifierTest {

    @Autowired
    private SearchInputClassifier searchInputClassifier;

    @Test
    void 초성이_포함되어_있을_경우_True_반환() {
        // given
        final String input = "ㅈㅁ";

        // when
        final boolean result = searchInputClassifier.isChosungQuery(input);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 초성이_포함되어_있지_않을_경우_False_반환() {
        // given
        final String input = "테스트";

        // when
        final boolean result = searchInputClassifier.isChosungQuery(input);

        // then
        assertThat(result).isFalse();
    }
}