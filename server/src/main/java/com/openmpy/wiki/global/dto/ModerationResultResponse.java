package com.openmpy.wiki.global.dto;

public record ModerationResultResponse(
        double weightedScore,
        double maxScore,
        double tieredScore,
        double flagBasedScore,
        double averageScore
) {
}
