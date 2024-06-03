package io.github.plantaest.feverfew.dto.common;

import java.util.List;

public record ListResponse<T>(
        int currentItemCount,
        int itemsPerPage,
        long totalItems,
        int pageIndex,
        int totalPages,
        List<T> items
) {}
