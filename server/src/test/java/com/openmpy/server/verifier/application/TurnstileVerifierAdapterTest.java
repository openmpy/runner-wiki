package com.openmpy.server.verifier.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.openmpy.server.global.properties.CloudflareProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class TurnstileVerifierAdapterTest {

    private TurnstileVerifierAdapter turnstileVerifierAdapter;
    private MockRestServiceServer mockServer;
    private CloudflareProperties cloudflareProperties;

    @BeforeEach
    void setUp() {
        final RestClient.Builder builder = RestClient.builder();
        final RestClient restClient = builder.build();

        mockServer = MockRestServiceServer.bindTo(builder).build();
        cloudflareProperties = mock(CloudflareProperties.class);
        turnstileVerifierAdapter = new TurnstileVerifierAdapter(cloudflareProperties, restClient);
    }

    @DisplayName("token이 null 또는 빈 값이면 false를 반환한다.")
    @ParameterizedTest(name = "입력: {0}")
    @NullAndEmptySource
    void turnstile_verifier_adapter_test_01(final String input) {
        // when
        final boolean verify = turnstileVerifierAdapter.verify(input, "127.0.0.1");

        // then
        assertThat(verify).isFalse();
    }

    @DisplayName("Cloudflare 응답이 success일 경우 true를 반환한다.")
    @Test
    void turnstile_verifier_adapter_test_02() {
        // stub
        when(cloudflareProperties.turnstileSecretKey()).thenReturn(
            "1x0000000000000000000000000000000AA"
        );

        // given & when
        final boolean verify = turnstileVerifierAdapter.verify(
            "token",
            "127.0.0.1"
        );

        // then
        assertThat(verify).isTrue();
    }

    @DisplayName("Cloudflare 응답이 fail일 경우 false를 반환한다.")
    @Test
    void turnstile_verifier_adapter_test_03() {
        // stub
        when(cloudflareProperties.turnstileSecretKey()).thenReturn(
            "2x0000000000000000000000000000000AA"
        );

        // given & when
        final boolean verify = turnstileVerifierAdapter.verify(
            "token",
            "127.0.0.1"
        );

        // then
        assertThat(verify).isFalse();
    }
}