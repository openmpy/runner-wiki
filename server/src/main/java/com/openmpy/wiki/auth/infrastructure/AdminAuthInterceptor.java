package com.openmpy.wiki.auth.infrastructure;

import static com.openmpy.wiki.auth.application.JwtService.ACCESS_TOKEN;

import com.openmpy.wiki.auth.application.JwtService;
import com.openmpy.wiki.global.utils.CookieManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean preHandle(
            final HttpServletRequest request, final HttpServletResponse response, final Object handler
    ) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        final String requestURI = request.getRequestURI();
        final boolean hasAdminPath = requestURI.contains("/admin");

        if (hasAdminPath) {
            final String accessToken = CookieManager.getJWT(request, ACCESS_TOKEN);

            if (accessToken == null) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "쿠키에서 Access Token을 찾을 수 없습니다.");
                return false;
            }

            if (!jwtService.validateToken(accessToken)) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Access Token이 유효하지 않습니다.");
                return false;
            }

            try {
                final String role = jwtService.getRole(accessToken);

                if (!"admin".equalsIgnoreCase(role)) {
                    sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "어드민 권한이 필요합니다.");
                    return false;
                }
            } catch (final Exception e) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "토큰에서 역할 추출에 실패했습니다.");
                return false;
            }
        }
        return true;
    }

    private void sendErrorResponse(
            final HttpServletResponse response, final int statusCode, final String message
    ) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"" + message + "\"}");
    }
}
