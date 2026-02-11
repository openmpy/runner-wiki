package com.openmpy.server.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class ClientIpResolverTest {

    @DisplayName("X-Forwarded-For 헤더가 존재하면 해당 IP를 반환한다.")
    @Test
    void client_ip_resolver_test_01() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest();

        request.addHeader("X-Forwarded-For", "192.168.0.1");
        request.setRemoteAddr("10.10.10.10");

        // when
        final String result = ClientIpResolver.getClientIp(request);

        // then
        assertThat(result).isEqualTo("192.168.0.1");
    }

    @DisplayName("X-Forwarded-For에 여러 IP가 있으면 첫 번째 IP만 반환한다.")
    @Test
    void client_ip_resolver_test_02() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest();

        request.addHeader("X-Forwarded-For", "192.168.0.1, 10.0.0.1, 127.0.0.1");
        request.setRemoteAddr("10.10.10.10");

        // when
        final String result = ClientIpResolver.getClientIp(request);

        // then
        assertThat(result).isEqualTo("192.168.0.1");
    }

    @DisplayName("헤더 값이 unknown이면 무시하고 다음 후보 헤더를 본다.")
    @Test
    void client_ip_resolver_test_03() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest();

        request.addHeader("X-Forwarded-For", "unknown");
        request.addHeader("X-Real-IP", "203.0.113.10");
        request.setRemoteAddr("10.10.10.10");

        // when
        final String result = ClientIpResolver.getClientIp(request);

        // then
        assertThat(result).isEqualTo("203.0.113.10");
    }

    @DisplayName("헤더 값이 모두 무효면 RemoteAddr를 반환한다.")
    @Test
    void client_ip_resolver_test_04() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest();

        request.addHeader("X-Forwarded-For", "");
        request.addHeader("X-Real-IP", "");
        request.addHeader("Proxy-Client-IP", "");
        request.addHeader("WL-Proxy-Client-IP", "");
        request.addHeader("HTTP_CLIENT_IP", "");
        request.addHeader("HTTP_X_FORWARDED_FOR", "");
        request.setRemoteAddr("8.8.8.8");

        // when
        final String result = ClientIpResolver.getClientIp(request);

        // then
        assertThat(result).isEqualTo("8.8.8.8");
    }

    @DisplayName("X-Forwarded-For가 없으면 다음 후보 헤더를 사용한다.")
    @Test
    void client_ip_resolver_test_05() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest();

        request.addHeader("X-Real-IP", "1.2.3.4");
        request.setRemoteAddr("8.8.8.8");

        // when
        final String result = ClientIpResolver.getClientIp(request);

        // then
        assertThat(result).isEqualTo("1.2.3.4");
    }

    @DisplayName("헤더 값 앞뒤 공백은 trim 된다.")
    @Test
    void client_ip_resolver_test_06() {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest();

        request.addHeader("X-Forwarded-For", "   192.168.0.1   ");
        request.setRemoteAddr("8.8.8.8");

        // when
        final String result = ClientIpResolver.getClientIp(request);

        // then
        assertThat(result).isEqualTo("192.168.0.1");
    }
}