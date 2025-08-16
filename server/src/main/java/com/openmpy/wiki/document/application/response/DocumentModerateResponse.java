package com.openmpy.wiki.document.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record DocumentModerateResponse(
        String id,
        String model,
        List<ModerationResult> results
) {

    public record ModerationResult(
            boolean flagged,
            Categories categories,

            @JsonProperty("category_scores")
            CategoryScores categoryScores
    ) {
    }

    public record Categories(
            boolean sexual,
            boolean hate,
            boolean harassment,

            @JsonProperty("self-harm")
            boolean selfHarm,

            @JsonProperty("sexual/minors")
            boolean sexualMinors,

            @JsonProperty("hate/threatening")
            boolean hateThreatening,

            @JsonProperty("violence/graphic")
            boolean violenceGraphic,

            @JsonProperty("self-harm/intent")
            boolean selfHarmIntent,

            @JsonProperty("self-harm/instructions")
            boolean selfHarmInstructions,

            @JsonProperty("harassment/threatening")
            boolean harassmentThreatening,

            boolean violence
    ) {
    }

    public record CategoryScores(
            double sexual,
            double hate,
            double harassment,

            @JsonProperty("self-harm")
            double selfHarm,

            @JsonProperty("sexual/minors")
            double sexualMinors,

            @JsonProperty("hate/threatening")
            double hateThreatening,

            @JsonProperty("violence/graphic")
            double violenceGraphic,

            @JsonProperty("self-harm/intent")
            double selfHarmIntent,

            @JsonProperty("self-harm/instructions")
            double selfHarmInstructions,

            @JsonProperty("harassment/threatening")
            double harassmentThreatening,

            double violence
    ) {
    }
}
