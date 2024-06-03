package io.github.plantaest.feverfew.dto.common;

import java.util.List;

public class ListResponseSchema<T> {
    private int currentItemCount;
    private int itemsPerPage;
    private long totalItems;
    private int pageIndex;
    private int totalPages;
    private List<T> items;
}
