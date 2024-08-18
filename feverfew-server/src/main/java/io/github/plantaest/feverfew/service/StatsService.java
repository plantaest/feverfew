package io.github.plantaest.feverfew.service;

import io.github.plantaest.feverfew.dto.common.AppResponse;
import io.github.plantaest.feverfew.dto.response.GetAllStatsResponse;
import io.github.plantaest.feverfew.repository.CheckRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StatsService {

    @Inject
    CheckRepository checkRepository;

    public AppResponse<GetAllStatsResponse> getAllStats() {
        GetAllStatsResponse getAllStatsResponse = checkRepository.getAllStats();
        return AppResponse.ok(getAllStatsResponse);
    }

}
