package com.openmpy.wiki.document.domain.constants;

import com.openmpy.wiki.global.exception.CustomException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum DocumentCategory {

    RUNNER("런너"), GUILD("길드");

    private final String value;

    DocumentCategory(final String value) {
        this.value = value;
    }

    public static DocumentCategory from(final String name) {
        return Arrays.stream(DocumentCategory.values())
                .filter(category -> category.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new CustomException("찾을 수 없는 문서 카테고리입니다."));
    }

    public static DocumentCategory of(final String value) {
        return Arrays.stream(DocumentCategory.values())
                .filter(category -> category.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new CustomException("찾을 수 없는 문서 카테고리입니다."));
    }
}
