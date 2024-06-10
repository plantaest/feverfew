package io.github.plantaest.feverfew.dto.common;

import java.util.List;

public class ListResponseSchema<T> {
    private int pageIndex;
    private int itemsPerPage;
    private long totalItems;
    private int totalPages;
    private int currentItemCount;
    private List<T> items;
}
