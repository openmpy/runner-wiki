package com.openmpy.server.global.dto;

import java.util.List;

public record SliceResponse<T>(
    List<T> payload,
    int page,
    int size,
    boolean hasNext,
    boolean hasPrevious
) {

}
