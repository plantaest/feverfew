package io.github.plantaest.feverfew.dto.response;

public record GetAllStatsResponse(
        Integer totalChecks,
        Integer totalAnonymousUsers,
        Integer totalWikis,
        Integer totalPages,
        Long totalDurationInMillis,
        Integer totalLinks,
        Integer totalIgnoredLinks,
        Integer totalWorkingLinks,
        Integer totalBrokenLinks
) {}
