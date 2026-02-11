package com.openmpy.server.global.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ClientIpResolver {

    private static final List<String> IP_HEADER_CANDIDATES = Arrays.asList(
        "X-Forwarded-For",
        "X-Real-IP",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_CLIENT_IP",
        "HTTP_X_FORWARDED_FOR"
    );

    public static String getClientIp(final HttpServletRequest request) {
        for (final String header : IP_HEADER_CANDIDATES) {
            final String ip = request.getHeader(header);

            if (isValidIp(ip)) {
                return extractFirstIp(ip);
            }
        }

        return request.getRemoteAddr();
    }

    private static boolean isValidIp(final String ip) {
        return ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip);
    }

    private static String extractFirstIp(final String ip) {
        if (ip.contains(",")) {
            return ip.split(",")[0].trim();
        }

        return ip.trim();
    }
}
