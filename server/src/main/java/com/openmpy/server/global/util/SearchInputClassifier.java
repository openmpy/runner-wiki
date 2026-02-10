package com.openmpy.server.global.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class SearchInputClassifier {

    public static boolean isChosungQuery(final String input) {
        if (input == null || input.isBlank()) {
            return false;
        }

        for (final char c : input.toCharArray()) {
            if (c >= 'ㄱ' && c <= 'ㅎ') {
                continue;
            }
            return false;
        }
        return true;
    }
}
