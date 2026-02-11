package com.openmpy.server.global.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public final class KoreanChosung {

    private static final char[] CHOSUNG = {
        'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ',
        'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };
    private static final char HANGUL_BASE = 0xAC00; // '가'
    private static final char HANGUL_LAST = 0xD7A3; // '힣'
    private static final int CHOSUNG_INTERVAL = 21 * 28;

    public static String extract(final String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        final StringBuilder sb = new StringBuilder(input.length());

        for (int i = 0; i < input.length(); i++) {
            final char c = input.charAt(i);

            if (Character.isWhitespace(c)) {
                continue;
            }
            if (c >= HANGUL_BASE && c <= HANGUL_LAST) {
                final int syllableIndex = c - HANGUL_BASE;
                final int chosungIndex = syllableIndex / CHOSUNG_INTERVAL;

                sb.append(CHOSUNG[chosungIndex]);
                continue;
            }

            sb.append(c);
        }
        return sb.toString();
    }
}
