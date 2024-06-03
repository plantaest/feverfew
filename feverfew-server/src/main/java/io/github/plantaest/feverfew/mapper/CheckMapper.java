package io.github.plantaest.feverfew.mapper;

import com.github.f4b6a3.tsid.TsidFactory;
import io.github.plantaest.feverfew.dto.request.CreateCheckRequest;
import io.github.plantaest.feverfew.dto.response.CreateCheckResponse;
import io.github.plantaest.feverfew.dto.response.CreateCheckResponseBuilder;
import io.github.plantaest.feverfew.entity.Check;
import io.github.plantaest.feverfew.entity.CheckBuilder;
import io.github.plantaest.feverfew.enumeration.CheckStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;

@ApplicationScoped
public class CheckMapper {

    @Inject
    TsidFactory tsidFactory;

    public Check toEntity(CreateCheckRequest request) {
        Instant now = Instant.now();

        return CheckBuilder.builder()
                .id(tsidFactory.create().toLong())
                .createdAt(now)
                .updatedAt(now)
                .createdBy(null)
                .updatedBy(null)
                .wikiId(request.wikiId())
                .pageTitle(request.pageTitle())
                .pageRevisionId(0L)
                .status(CheckStatus.SUCCESS)
                .build();
    }

    public CreateCheckResponse toResponse(Check check) {
        return CreateCheckResponseBuilder.builder()
                .id(check.id().toString())
                .createdAt(check.createdAt())
                .updatedAt(check.updatedAt())
                .totalLinks(10)
                .build();
    }

}
