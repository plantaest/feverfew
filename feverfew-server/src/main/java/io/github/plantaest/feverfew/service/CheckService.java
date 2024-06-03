package io.github.plantaest.feverfew.service;

import io.github.plantaest.feverfew.dto.common.AppResponse;
import io.github.plantaest.feverfew.dto.request.CreateCheckRequest;
import io.github.plantaest.feverfew.dto.response.CreateCheckResponse;
import io.github.plantaest.feverfew.entity.Check;
import io.github.plantaest.feverfew.helper.ExternalLink;
import io.github.plantaest.feverfew.helper.LinkHelper;
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

    public AppResponse<CreateCheckResponse> createCheck(CreateCheckRequest request) {
        // Step 1. Get page content
        String pageHtmlContent = "This is page HTML content.";

        // Step 2. Extract links
        List<ExternalLink> externalLinks = linkHelper.extractExternalLinks(pageHtmlContent);

        Check check = checkMapper.toEntity(request);
        CreateCheckResponse response = checkMapper.toResponse(check);
        return AppResponse.created(response);
    }

}
