package io.github.plantaest.feverfew.mapper;

import io.github.plantaest.feverfew.dto.response.CreateCheckResponse;
import io.github.plantaest.feverfew.dto.response.CreateCheckResponseBuilder;
import io.github.plantaest.feverfew.entity.Check;
import io.github.plantaest.feverfew.helper.EvaluationResult;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CheckMapper {

    public CreateCheckResponse toResponse(Check check, String serverName, List<EvaluationResult> evaluationResults) {
        return CreateCheckResponseBuilder.builder()
                .id(String.valueOf(check.id()))
                .createdAt(check.createdAt())
                .createdBy(check.createdBy())
                .wikiId(check.wikiId())
                .wikiServerName(serverName)
                .pageTitle(check.pageTitle())
                .pageRevisionId(check.pageRevisionId())
                .durationInMillis(check.durationInMillis())
                .totalLinks(check.totalLinks())
                .totalIgnoredLinks(check.totalIgnoredLinks())
                .totalSuccessLinks(check.totalSuccessLinks())
                .totalErrorLinks(check.totalErrorLinks())
                .totalWorkingLinks(check.totalWorkingLinks())
                .totalBrokenLinks(check.totalBrokenLinks())
                .results(evaluationResults)
                .build();
    }

}
