package com.openmpy.server.global.util;

public final class PageLimitCalculator {

    private PageLimitCalculator() {
    }

    public static int calculatePageLimit(final int page, final int pageSize, final int movablePageCount) {
        return ((page / movablePageCount) + 1) * pageSize * movablePageCount + 1;
    }
}
