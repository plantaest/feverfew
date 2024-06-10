package io.github.plantaest.feverfew.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.plantaest.composite.Wikis;
import io.github.plantaest.feverfew.dto.response.CreateCheckResponse;
import io.github.plantaest.feverfew.dto.response.CreateCheckResponseBuilder;
import io.github.plantaest.feverfew.dto.response.GetListCheckResponse;
import io.github.plantaest.feverfew.dto.response.GetListCheckResponseBuilder;
import io.github.plantaest.feverfew.dto.response.GetOneCheckResponse;
import io.github.plantaest.feverfew.dto.response.GetOneCheckResponseBuilder;
import io.github.plantaest.feverfew.entity.Check;
import io.github.plantaest.feverfew.helper.CompressionHelper;
import io.github.plantaest.feverfew.helper.EvaluationResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CheckMapper {

    @Inject
    Wikis wikis;
    @Inject
    CompressionHelper compressionHelper;

    public CreateCheckResponse toCreateResponse(Check check, String serverName, List<EvaluationResult> evaluationResults) {
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

    public GetOneCheckResponse toGetOneResponse(Check check) {
        List<EvaluationResult> evaluationResults = compressionHelper.schemaToTarget(
                compressionHelper.decompressJson(check.results(), new TypeReference<>() {})
        );

        return GetOneCheckResponseBuilder.builder()
                .id(String.valueOf(check.id()))
                .createdAt(check.createdAt())
                .createdBy(check.createdBy())
                .wikiId(check.wikiId())
                .wikiServerName(wikis.getWiki(check.wikiId()).config().serverName())
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

    public GetListCheckResponse toGetListResponse(Check check) {
        return GetListCheckResponseBuilder.builder()
                .id(String.valueOf(check.id()))
                .createdAt(check.createdAt())
                .createdBy(check.createdBy())
                .wikiId(check.wikiId())
                .wikiServerName(wikis.getWiki(check.wikiId()).config().serverName())
                .pageTitle(check.pageTitle())
                .pageRevisionId(check.pageRevisionId())
                .durationInMillis(check.durationInMillis())
                .totalLinks(check.totalLinks())
                .totalIgnoredLinks(check.totalIgnoredLinks())
                .totalSuccessLinks(check.totalSuccessLinks())
                .totalErrorLinks(check.totalErrorLinks())
                .totalWorkingLinks(check.totalWorkingLinks())
                .totalBrokenLinks(check.totalBrokenLinks())
                .build();
    }

}
