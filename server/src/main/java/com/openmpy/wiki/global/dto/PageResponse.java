package com.openmpy.wiki.global.dto;

public record PageResponse<T>(
        T items,
        int page,
        int size,
        long totalCount
) {
}
