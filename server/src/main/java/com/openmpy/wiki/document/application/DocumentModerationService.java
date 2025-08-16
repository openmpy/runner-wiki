package com.openmpy.wiki.document.application;

import com.openmpy.wiki.document.application.request.DocumentModerateRequest;
import com.openmpy.wiki.document.application.response.DocumentModerateResponse;
import com.openmpy.wiki.document.application.response.DocumentModerateResponse.Categories;
import com.openmpy.wiki.document.application.response.DocumentModerateResponse.CategoryScores;
import com.openmpy.wiki.document.application.response.DocumentModerateResponse.ModerationResult;
import com.openmpy.wiki.document.application.response.DocumentModerateResultResponse;
import com.openmpy.wiki.global.dto.ModerationResultResponse;
import com.openmpy.wiki.global.utils.ModerationScoreCalculator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
public class DocumentModerationService {

    private static final String MODERATION_API_URL = "https://api.openai.com/v1/moderations";

    private final RestClient restClient = RestClient.create();

    @Value("${openai.api-key}")
    private String apiKey;

    public DocumentModerateResultResponse getViolenceScore(final DocumentModerateRequest request) {
        final Consumer<HttpHeaders> headers = httpHeaders -> {
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/json");
            httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);
        };

        final DocumentModerateResponse response = restClient.post()
                .uri(MODERATION_API_URL)
                .headers(headers)
                .body(request)
                .retrieve()
                .body(DocumentModerateResponse.class);

        if (response == null) {
            return new DocumentModerateResultResponse(null);
        }

        final ModerationResult result = response.results().getFirst();
        final Categories categories = result.categories();
        final CategoryScores scores = result.categoryScores();

        final Map<String, Boolean> flaggedCategories = getFlaggedCategories(categories);
        final Map<String, Double> categoryScores = getCategoryScores(scores);

        final ModerationResultResponse resultResponse = ModerationScoreCalculator.calculateComprehensiveScore(
                flaggedCategories, categoryScores
        );
        return new DocumentModerateResultResponse(resultResponse.averageScore());
    }

    @NotNull
    private static Map<String, Boolean> getFlaggedCategories(final Categories categories) {
        final Map<String, Boolean> flaggedCategories = new HashMap<>();
        flaggedCategories.put("sexual", categories.sexual());
        flaggedCategories.put("hate", categories.hate());
        flaggedCategories.put("harassment", categories.harassment());
        flaggedCategories.put("self-harm", categories.selfHarm());
        flaggedCategories.put("sexual/minors", categories.sexual());
        flaggedCategories.put("hate/threatening", categories.hate());
        flaggedCategories.put("violence/graphic", categories.violenceGraphic());
        flaggedCategories.put("self-harm/intent", categories.selfHarmIntent());
        flaggedCategories.put("self-harm/instructions", categories.selfHarmInstructions());
        flaggedCategories.put("harassment/threatening", categories.harassmentThreatening());
        flaggedCategories.put("violence", categories.violence());
        return flaggedCategories;
    }

    @NotNull
    private static Map<String, Double> getCategoryScores(final CategoryScores scores) {
        final Map<String, Double> categoryScores = new HashMap<>();
        categoryScores.put("sexual", scores.sexual());
        categoryScores.put("hate", scores.hate());
        categoryScores.put("harassment", scores.harassment());
        categoryScores.put("self-harm", scores.selfHarm());
        categoryScores.put("sexual/minors", scores.sexualMinors());
        categoryScores.put("hate/threatening", scores.hateThreatening());
        categoryScores.put("violence/graphic", scores.violenceGraphic());
        categoryScores.put("self-harm/intent", scores.selfHarmIntent());
        categoryScores.put("self-harm/instructions", scores.selfHarmInstructions());
        categoryScores.put("harassment/threatening", scores.harassmentThreatening());
        categoryScores.put("violence", scores.violence());
        return categoryScores;
    }
}
