package com.openmpy.server.global.dto.response;

import java.util.List;

public record PageResponse<T>(
        List<T> payload,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {

    public static <T> PageResponse<T> of(
            final List<T> payload,
            final int page,
            final int size,
            final long totalElements
    ) {
        final int totalPages = (int) Math.ceil((double) totalElements / size);

        return new PageResponse<>(
                payload,
                page,
                size,
                totalElements,
                totalPages,
                page < totalPages - 1,
                page > 0
        );
    }
}
