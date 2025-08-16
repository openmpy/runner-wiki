package com.openmpy.wiki.global.utils;

import com.openmpy.wiki.global.dto.ModerationResultResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModerationScoreCalculator {

    // 카테고리별 가중치 정의
    private static final Map<String, Double> CATEGORY_WEIGHTS = Map.of(
            "violence", 0.25,
            "harassment/threatening", 0.20,
            "harassment", 0.15,
            "hate", 0.15,
            "hate/threatening", 0.10,
            "sexual", 0.05,
            "sexual/minors", 0.05,
            "violence/graphic", 0.03,
            "self-harm/intent", 0.01,
            "self-harm/instructions", 0.01
    );

    // 심각도별 카테고리 그룹
    private static final List<String> CRITICAL_CATEGORIES = Arrays.asList(
            "violence", "harassment/threatening", "sexual/minors"
    );
    private static final List<String> HIGH_CATEGORIES = Arrays.asList(
            "harassment", "hate", "hate/threatening"
    );
    private static final List<String> MEDIUM_CATEGORIES = Arrays.asList(
            "sexual", "violence/graphic"
    );
    private static final List<String> LOW_CATEGORIES = Arrays.asList(
            "self-harm/intent", "self-harm/instructions"
    );

    public static double calculateWeightedScore(final Map<String, Double> categoryScores) {
        double weightedSum = 0.0;

        for (Map.Entry<String, Double> entry : categoryScores.entrySet()) {
            final String category = entry.getKey();
            final Double score = entry.getValue();
            final Double weight = CATEGORY_WEIGHTS.get(category);

            if (weight != null && score != null) {
                weightedSum += score * weight;
            }
        }

        return Math.min(100.0, weightedSum * 100);
    }

    public static double calculateMaxScore(final Map<String, Double> categoryScores) {
        double maxScore = 0.0;

        for (final Double score : categoryScores.values()) {
            if (score != null) {
                maxScore = Math.max(maxScore, score);
            }
        }

        return maxScore * 100;
    }

    public static double calculateTieredScore(final Map<String, Double> categoryScores) {
        final double criticalMax = getMaxScoreFromCategories(categoryScores, CRITICAL_CATEGORIES);
        final double highMax = getMaxScoreFromCategories(categoryScores, HIGH_CATEGORIES);
        final double mediumMax = getMaxScoreFromCategories(categoryScores, MEDIUM_CATEGORIES);
        final double lowMax = getMaxScoreFromCategories(categoryScores, LOW_CATEGORIES);

        double score = (criticalMax * 0.5) + (highMax * 0.3) + (mediumMax * 0.15) + (lowMax * 0.05);
        return Math.min(100.0, score * 100);
    }

    public static double calculateFlagBasedScore(
            final Map<String, Boolean> flaggedCategories,
            final Map<String, Double> categoryScores
    ) {
        double score = 0.0;
        int flaggedCount = 0;

        for (Map.Entry<String, Boolean> entry : flaggedCategories.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                final String category = entry.getKey();
                final Double categoryScore = categoryScores.get(category);
                final Double weight = CATEGORY_WEIGHTS.get(category);

                if (categoryScore != null && weight != null) {
                    score += categoryScore * weight * 2;
                    flaggedCount++;
                }
            }
        }

        score += flaggedCount * 0.1;
        return Math.min(100.0, score * 100);
    }

    public static ModerationResultResponse calculateComprehensiveScore(
            final Map<String, Boolean> flaggedCategories,
            final Map<String, Double> categoryScores
    ) {
        final double weightedScore = calculateWeightedScore(categoryScores);
        final double maxScore = calculateMaxScore(categoryScores);
        final double tieredScore = calculateTieredScore(categoryScores);
        final double flagBasedScore = calculateFlagBasedScore(flaggedCategories, categoryScores);
        final double averageScore = (weightedScore + maxScore + tieredScore + flagBasedScore) / 4.0;

        return new ModerationResultResponse(weightedScore, maxScore, tieredScore, flagBasedScore, averageScore);
    }

    private static double getMaxScoreFromCategories(
            final Map<String, Double> categoryScores,
            final List<String> categories
    ) {
        double maxScore = 0.0;

        for (String category : categories) {
            final Double score = categoryScores.get(category);

            if (score != null) {
                maxScore = Math.max(maxScore, score);
            }
        }
        return maxScore;
    }
}
