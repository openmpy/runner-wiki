package com.openmpy.wiki.global.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

    public static String getJWT(final HttpServletRequest servletRequest, final String cookieName) {
        if (servletRequest.getCookies() != null) {
            for (final Cookie cookie : servletRequest.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
