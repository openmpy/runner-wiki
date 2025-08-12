package com.openmpy.wiki.global.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientIpExtractor {

    private static final String[] HEADERS = new String[]{
            "CF-Connecting-IP",
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };

    public static String getClientIp(final HttpServletRequest request) {
        for (final String header : HEADERS) {
            final String ip = request.getHeader(header);

            if (ip != null && !ip.isBlank()) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}
