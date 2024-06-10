package io.github.plantaest.feverfew.helper;

public record Pagination(
        int pageIndex,
        int itemsPerPage,
        long totalItems,
        int totalPages,
        int currentItemCount,
        int limit,
        long offset
) {

    public static Pagination of(int pageIndex, int itemsPerPage, long totalItems) {
        int totalPages = calculateTotalPages(totalItems, itemsPerPage);
        int currentItemCount = calculateCurrentItemCount(pageIndex, itemsPerPage, totalItems);
        long offset = calculateOffset(pageIndex, itemsPerPage);
        return new Pagination(pageIndex, itemsPerPage, totalItems, totalPages, currentItemCount, itemsPerPage, offset);
    }

    public static int calculateTotalPages(long totalItems, int itemsPerPage) {
        return (int) Math.ceil((double) totalItems / itemsPerPage);
    }

    public static int calculateCurrentItemCount(int pageIndex, int itemsPerPage, long totalItems) {
        int startIndex = (pageIndex - 1) * itemsPerPage;
        return (int) Math.min(itemsPerPage, totalItems - startIndex);
    }

    public static long calculateOffset(int pageIndex, int itemsPerPage) {
        return (long) (pageIndex - 1) * itemsPerPage;
    }

}
