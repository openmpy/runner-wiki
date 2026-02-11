package com.openmpy.server.global.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public final class PageLimitCalculator {

    public static int calculatePageLimit(
        final int page,             // 현재 페이지 번호
        final int pageSize,         // 한 페이지당 데이터 수
        final int movablePageCount  // 한 번에 보여줄 페이지 개수
    ) {
        return ((page / movablePageCount) + 1) * pageSize * movablePageCount + 1;
    }
}
