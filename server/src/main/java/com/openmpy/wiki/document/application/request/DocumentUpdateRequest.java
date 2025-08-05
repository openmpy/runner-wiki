package com.openmpy.wiki.document.application.request;

import java.util.List;

public record DocumentUpdateRequest(
        String author, String content, List<String> imageIds
) {
}
