package com.openmpy.wiki.global.dto;

public record PageResponse<T>(
        T content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {
}
