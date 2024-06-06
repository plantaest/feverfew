package io.github.plantaest.feverfew.service;

import de.siegmar.fastcsv.writer.CsvWriter;
import io.github.plantaest.composite.Wiki;
import io.github.plantaest.composite.Wikis;
import io.github.plantaest.feverfew.dto.common.AppResponse;
import io.github.plantaest.feverfew.dto.request.CreateCheckRequest;
import io.github.plantaest.feverfew.dto.request.ExportFeaturesAsCsvRequest;
import io.github.plantaest.feverfew.dto.response.CreateCheckResponse;
import io.github.plantaest.feverfew.entity.Check;
import io.github.plantaest.feverfew.helper.ExternalLink;
import io.github.plantaest.feverfew.helper.LinkHelper;
import io.github.plantaest.feverfew.helper.RequestResult;
import io.github.plantaest.feverfew.mapper.CheckMapper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CheckService {

    @Inject
    CheckMapper checkMapper;

    @Inject
    LinkHelper linkHelper;

    @Inject
    Wikis wikis;

    public AppResponse<CreateCheckResponse> createCheck(CreateCheckRequest request) {
        Wiki wiki = wikis.getWiki(request.wikiId());

        // Step 1. Get page content
        String pageHtmlContent = wiki.page(request.pageTitle()).html();

        // Step 2. Extract links
        List<ExternalLink> externalLinks = linkHelper.extractExternalLinks(pageHtmlContent);

        // Step 3. Call & collect link information
        List<RequestResult> requestResults = linkHelper.requestExternalLinks(externalLinks);
        Log.debugf("Sorted durations: %s", requestResults.stream()
                .mapToDouble(RequestResult::requestDuration)
                .sorted()
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" ")));

        // Step 4. Categorize links

        Check check = checkMapper.toEntity(request);
        CreateCheckResponse response = checkMapper.toResponse(check);
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
                "redirect_to_homepage"
        );

        for (var result : requestResults) {
            csv.writeRecord(
                    result.requestUrl(),
                    result.type().toString(),
                    String.valueOf(result.requestDuration()),
                    String.valueOf(result.responseStatus()),
                    String.valueOf(result.contentLength()),
                    String.valueOf(result.containsPageNotFoundWords()),
                    String.valueOf(result.containsPaywallWords()),
                    String.valueOf(result.containsDomainExpiredWords()),
                    String.valueOf(result.redirects().size()),
                    String.valueOf(result.redirectToHomepage())
            );
        }

        return sw.toString();
    }

}
