package com.openmpy.server.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PageLimitCalculatorTest {

    @DisplayName("첫 번째 페이지 블록에서는 첫 블록 limit을 반환한다.")
    @Test
    void page_limit_calculator_test_01() {
        // given
        final int page = 0;
        final int pageSize = 20;
        final int movablePageCount = 5;

        // when
        final int limit = PageLimitCalculator.calculatePageLimit(
            page, pageSize, movablePageCount
        );

        // then
        assertThat(limit).isEqualTo(101);
    }

    @DisplayName("같은 블록 내 페이지들은 동일한 limit을 가진다.")
    @Test
    void page_limit_calculator_test_02() {
        // given
        final int pageSize = 20;
        final int movablePageCount = 5;

        // when
        final int limit1 = PageLimitCalculator.calculatePageLimit(
            1, pageSize, movablePageCount
        );
        final int limit2 = PageLimitCalculator.calculatePageLimit(
            3, pageSize, movablePageCount
        );
        final int limit3 = PageLimitCalculator.calculatePageLimit(
            4, pageSize, movablePageCount
        );

        // then
        assertThat(limit1).isEqualTo(101);
        assertThat(limit2).isEqualTo(101);
        assertThat(limit3).isEqualTo(101);
    }

    @DisplayName("두 번째 페이지 블록에서는 두 번째 블록 limit을 반환한다.")
    @Test
    void page_limit_calculator_test_03() {
        // given
        final int page = 5;
        final int pageSize = 20;
        final int movablePageCount = 5;

        // when
        final int limit = PageLimitCalculator.calculatePageLimit(
            page, pageSize, movablePageCount
        );

        // then
        assertThat(limit).isEqualTo(201);
    }

    @DisplayName("블록 경계값에서도 올바른 limit을 계산한다.")
    @Test
    void page_limit_calculator_test_04() {
        // given
        final int pageSize = 10;
        final int movablePageCount = 3;

        // when
        final int limit1 = PageLimitCalculator.calculatePageLimit(
            2, pageSize, movablePageCount
        );
        final int limit2 = PageLimitCalculator.calculatePageLimit(
            3, pageSize, movablePageCount
        );

        // then
        assertThat(limit1).isEqualTo(31);
        assertThat(limit2).isEqualTo(61);
    }

    @DisplayName("큰 페이지 번호에서도 정상적으로 limit을 계산한다.")
    @Test
    void page_limit_calculator_test_05() {
        // given
        final int page = 123;
        final int pageSize = 50;
        final int movablePageCount = 10;

        // when
        final int limit = PageLimitCalculator.calculatePageLimit(
            page, pageSize, movablePageCount
        );

        // then
        assertThat(limit).isEqualTo(6501);
    }
}