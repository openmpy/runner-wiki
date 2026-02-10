package com.openmpy.server.verifier.application;

import com.openmpy.server.global.properties.CloudflareProperties;
import com.openmpy.server.verifier.application.port.VerifierPort;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
public class TurnstileVerifierAdapter implements VerifierPort {

    private static final String TURNSTILE_VERIFY_URL = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

    private final CloudflareProperties cloudflareProperties;
    private final RestClient restClient = RestClient.create();

    @Override
    public boolean verify(final String token, final String clientIp) {
        if (token == null || token.isBlank()) {
            return false;
        }

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", cloudflareProperties.turnstileSecretKey());
        params.add("response", token);
        params.add("remoteip", clientIp);

        final Map<String, Object> response = restClient.post()
            .uri(TURNSTILE_VERIFY_URL)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(params)
            .retrieve()
            .body(new ParameterizedTypeReference<>() {
            });

        if (response == null) {
            return false;
        }
        return Boolean.TRUE.equals(response.get("success"));
    }
}
