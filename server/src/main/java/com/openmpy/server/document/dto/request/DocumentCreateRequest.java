package com.openmpy.server.document.dto.request;

import com.openmpy.server.document.domain.type.DocumentCategory;
import jakarta.annotation.Nullable;
import java.util.List;

public record DocumentCreateRequest(
    String title,
    DocumentCategory category,
    String author,
    String content,
    @Nullable List<Long> imageIds,
    String token
) {

}
