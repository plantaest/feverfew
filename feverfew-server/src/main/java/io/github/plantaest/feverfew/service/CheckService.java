package io.github.plantaest.feverfew.service;

import io.github.plantaest.composite.Wiki;
import io.github.plantaest.composite.Wikis;
import io.github.plantaest.feverfew.dto.common.AppResponse;
import io.github.plantaest.feverfew.dto.request.CreateCheckRequest;
import io.github.plantaest.feverfew.dto.response.CreateCheckResponse;
import io.github.plantaest.feverfew.entity.Check;
import io.github.plantaest.feverfew.helper.ExternalLink;
import io.github.plantaest.feverfew.helper.LinkHelper;
import io.github.plantaest.feverfew.helper.RequestResult;
import io.github.plantaest.feverfew.mapper.CheckMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

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
        List<RequestResult> requestResults = linkHelper.requestLinks(externalLinks);

        Check check = checkMapper.toEntity(request);
        CreateCheckResponse response = checkMapper.toResponse(check);
        return AppResponse.created(response);
    }

}
