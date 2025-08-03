package com.openmpy.wiki.global.utils;

import jakarta.servlet.http.HttpServletRequest;

public final class IpAddressUtil {

    private IpAddressUtil() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    private static final String[] HEADERS = new String[]{
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
