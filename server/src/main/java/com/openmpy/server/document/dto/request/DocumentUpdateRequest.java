package com.openmpy.server.document.dto.request;

import jakarta.annotation.Nullable;
import java.util.List;

public record DocumentUpdateRequest(
    String author,
    String content,
    @Nullable List<Long> imageIds,
    String token
) {

}
