package io.github.plantaest.feverfew.service;

import com.github.f4b6a3.tsid.TsidFactory;
import de.siegmar.fastcsv.writer.CsvWriter;
import io.github.plantaest.composite.Wiki;
import io.github.plantaest.composite.Wikis;
import io.github.plantaest.composite.type.PageHtmlResult;
import io.github.plantaest.feverfew.config.AppConfig;
import io.github.plantaest.feverfew.config.filter.CookieFilter;
import io.github.plantaest.feverfew.dto.common.AppResponse;
import io.github.plantaest.feverfew.dto.common.ListResponse;
import io.github.plantaest.feverfew.dto.request.CreateCheckRequest;
import io.github.plantaest.feverfew.dto.request.ExportFeaturesAsCsvRequest;
import io.github.plantaest.feverfew.dto.response.CreateCheckResponse;
import io.github.plantaest.feverfew.dto.response.GetListCheckResponse;
import io.github.plantaest.feverfew.dto.response.GetOneCheckResponse;
import io.github.plantaest.feverfew.entity.Check;
import io.github.plantaest.feverfew.entity.CheckBuilder;
import io.github.plantaest.feverfew.helper.ClassificationResult;
import io.github.plantaest.feverfew.helper.Classifier;
import io.github.plantaest.feverfew.helper.CompressionHelper;
import io.github.plantaest.feverfew.helper.EvaluationResult;
import io.github.plantaest.feverfew.helper.EvaluationResultBuilder;
import io.github.plantaest.feverfew.helper.ExternalLink;
import io.github.plantaest.feverfew.helper.LinkHelper;
import io.github.plantaest.feverfew.helper.Pagination;
import io.github.plantaest.feverfew.helper.RequestResult;
import io.github.plantaest.feverfew.helper.TimeHelper;
import io.github.plantaest.feverfew.mapper.CheckMapper;
import io.github.plantaest.feverfew.repository.CheckRepository;
import io.quarkus.logging.Log;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Context;
import org.slf4j.MDC;

import java.io.StringWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class CheckService {

    @Inject
    CheckRepository checkRepository;
    @Inject
    CheckMapper checkMapper;
    @Inject
    LinkHelper linkHelper;
    @Inject
    Classifier classifier;
    @Inject
    TsidFactory tsidFactory;
    @Inject
    Wikis wikis;
    @Inject
    CompressionHelper compressionHelper;
    @Context
    HttpServerRequest httpServerRequest;
    @Inject
    AppConfig appConfig;

    public AppResponse<CreateCheckResponse> createCheck(CreateCheckRequest request) {
        Log.infof("Request body of createCheck: %s", request);

        long startTime = System.nanoTime();
        Wiki wiki = wikis.getWiki(request.wikiId());

        // Step 1. Get page content
        PageHtmlResult pageHtmlResult = wiki.page(request.pageTitle()).html();

        // Step 2. Extract links
        List<ExternalLink> externalLinks = linkHelper.extractExternalLinks(pageHtmlResult.html());

        // Step 3. Call & collect link information
        List<RequestResult> requestResults = linkHelper.requestExternalLinks(externalLinks);

        if (!requestResults.isEmpty()) {
            Log.debugf("Sorted durations: %s", requestResults.stream()
                    .mapToDouble(RequestResult::requestDuration)
                    .sorted()
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(" ")));
        }

        // Step 4. Categorize links
        List<ClassificationResult> classificationResults = classifier.classify(requestResults);

        // Step 5. Return response
        List<EvaluationResult> evaluationResults = new ArrayList<>();

        for (int i = 0; i < externalLinks.size(); i++) {
            var evaluationResult = EvaluationResultBuilder.builder()
                    .index(i + 1)
                    .link(externalLinks.get(i))
                    .requestResult(requestResults.get(i))
                    .classificationResult(classificationResults.get(i))
                    .build();
            evaluationResults.add(evaluationResult);
        }

        var totalIgnoredLinks = evaluationResults.stream()
                .filter(r -> r.requestResult().type() == RequestResult.Type.IGNORED)
                .count();
        var totalSuccessLinks = evaluationResults.stream()
                .filter(r -> r.requestResult().type() == RequestResult.Type.SUCCESS)
                .count();
        var totalErrorLinks = evaluationResults.stream()
                .filter(r -> r.requestResult().type() == RequestResult.Type.ERROR)
                .count();

        var nonIgnoredLinks = evaluationResults.stream()
                .filter(r -> r.requestResult().type() != RequestResult.Type.IGNORED)
                .toList();
        var totalWorkingLinks = nonIgnoredLinks.stream()
                .filter(r -> r.classificationResult().label() == 0L)
                .count();
        var totalBrokenLinks = nonIgnoredLinks.stream()
                .filter(r -> r.classificationResult().label() == 1L)
                .count();

        // Step 6. Create record & return response
        var check = CheckBuilder.builder()
                .id(tsidFactory.create().toLong())
                .createdAt(Instant.now())
                .createdBy(Long.parseLong(MDC.get(CookieFilter.ACTOR_ID)))
                .wikiId(request.wikiId())
                .pageTitle(pageHtmlResult.title())
                .pageRevisionId(pageHtmlResult.revisionId())
                .durationInMillis(TimeHelper.durationInMillis(startTime))
                .totalLinks(evaluationResults.size())
                .totalIgnoredLinks(Math.toIntExact(totalIgnoredLinks))
                .totalSuccessLinks(Math.toIntExact(totalSuccessLinks))
                .totalErrorLinks(Math.toIntExact(totalErrorLinks))
                .totalWorkingLinks(Math.toIntExact(totalWorkingLinks))
                .totalBrokenLinks(Math.toIntExact(totalBrokenLinks))
                .resultSchemaVersion(appConfig.currentResultSchemaVersion())
                .results(evaluationResults.isEmpty()
                        ? null
                        : compressionHelper.compressJson(compressionHelper.convertToSchema(evaluationResults)))
                .build();

        checkRepository.create(check);

        Log.infof("Successfully created check with id: %s", check.id());
        var response = checkMapper.toCreateResponse(check, wiki.config().serverName(), evaluationResults);
        return AppResponse.created(response);
    }

    public String exportFeaturesAsCsv(ExportFeaturesAsCsvRequest request) {
        // Call & collect link information
        List<RequestResult> requestResults = linkHelper.requestLinks(request.links());

        // Build CSV
        var sw = new StringWriter();
        var csv = CsvWriter.builder().build(sw);

        // Add headers
        csv.writeRecord(
                "link",
                "result_type",
                "request_duration",
                "response_status",
                "content_length",
                "contains_page_not_found_words",
                "contains_paywall_words",
                "contains_domain_expired_words",
                "number_of_redirects",
                "redirect_to_homepage",
                "simple_redirect",
                "timeout",
                "file_type"
        );

        for (int i = 0; i < requestResults.size(); i++) {
            var result = requestResults.get(i);
            csv.writeRecord(
                    request.links().get(i),
                    result.type().toString(),
                    String.valueOf(result.requestDuration()),
                    String.valueOf(result.responseStatus()),
                    String.valueOf(result.contentLength()),
                    String.valueOf(result.containsPageNotFoundWords()),
                    String.valueOf(result.containsPaywallWords()),
                    String.valueOf(result.containsDomainExpiredWords()),
                    String.valueOf(result.type() == RequestResult.Type.SUCCESS ? result.redirects().size() : -1),
                    String.valueOf(result.redirectToHomepage()),
                    String.valueOf(result.simpleRedirect()),
                    String.valueOf(result.timeout()),
                    result.fileType().toString()
            );
        }

        return sw.toString();
    }

    public AppResponse<GetOneCheckResponse> getOneCheck(Long id) {
        Log.infof("Request path params of getOneCheck: %s", Map.of("id", id));
        Check check = checkRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unable to retrieve check with id: " + id));
        var response = checkMapper.toGetOneResponse(check);
        return AppResponse.ok(response);
    }

    public AppResponse<ListResponse<GetListCheckResponse>> getListCheck(Integer page, Integer size) {
        Log.infof("Request query params of getListCheck: %s", Map.of("page", page, "size", size));
        long totalItems = checkRepository.count();
        var pagination = Pagination.of(page, size, totalItems);
        List<Check> checks = checkRepository.findAll(pagination.limit(), pagination.offset());
        List<GetListCheckResponse> responses = checks.stream().map(checkMapper::toGetListResponse).toList();
        return AppResponse.ok(ListResponse.of(responses, pagination));
    }

}
