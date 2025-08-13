package com.openmpy.wiki.global.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieManager {

    public static ResponseCookie createCookie(final String name, final String value, final String domain) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(3600)
                .sameSite("none")
                .domain(domain)
                .build();
    }

    public static ResponseCookie deleteCooke(final String name, final String domain) {
        return ResponseCookie.from(name, "")
                .path("/")
                .maxAge(0)
                .domain(domain)
                .build();
    }
}
