package com.openmpy.wiki.admin.domain.constants;

import com.openmpy.wiki.global.exception.CustomException;
import java.util.Arrays;

public enum AdminLogType {

    LOGIN, DELETE_DOCUMENT, READ_ONLY_DOCUMENT, DELETE_DOCUMENT_HISTORY;

    public static AdminLogType from(final String value) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new CustomException("찾을 수 없는 어드민 로그 타입입니다."));
    }
}
