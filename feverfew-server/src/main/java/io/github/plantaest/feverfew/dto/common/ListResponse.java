package io.github.plantaest.feverfew.dto.common;

import io.github.plantaest.feverfew.helper.Pagination;

import java.util.List;

public record ListResponse<T>(
        int pageIndex,
        int itemsPerPage,
        long totalItems,
        int totalPages,
        int currentItemCount,
        List<T> items
) {

    public static <T> ListResponse<T> of(List<T> items, Pagination pagination) {
        return new ListResponse<>(
                pagination.pageIndex(),
                pagination.itemsPerPage(),
                pagination.totalItems(),
                pagination.totalPages(),
                pagination.currentItemCount(),
                items
        );
    }

}
