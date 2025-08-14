package com.openmpy.wiki.document.domain.constants;

import com.openmpy.wiki.global.exception.CustomException;
import java.util.Arrays;

public enum DocumentStatus {

    ACTIVE, READ_ONLY;

    public static DocumentStatus from(final String name) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new CustomException("찾을 수 없는 문서 상태입니다."));
    }
}
