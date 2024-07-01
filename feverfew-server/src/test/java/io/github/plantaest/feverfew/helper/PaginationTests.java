package io.github.plantaest.feverfew.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaginationTests {

    @Test
    public void test_calculate_total_pages_with_exact_division() {
        long totalItems = 100;
        int itemsPerPage = 10;
        int expectedPages = 10;
        int actualPages = Pagination.calculateTotalPages(totalItems, itemsPerPage);
        assertEquals(expectedPages, actualPages);
    }

    @Test
    public void test_calculate_total_pages_with_remainder() {
        long totalItems = 101;
        int itemsPerPage = 10;
        int expectedPages = 11;
        int actualPages = Pagination.calculateTotalPages(totalItems, itemsPerPage);
        assertEquals(expectedPages, actualPages);
    }

    @Test
    public void test_calculate_total_pages_with_one_item() {
        long totalItems = 1;
        int itemsPerPage = 10;
        int expectedPages = 1;
        int actualPages = Pagination.calculateTotalPages(totalItems, itemsPerPage);
        assertEquals(expectedPages, actualPages);
    }

    @Test
    public void test_calculate_total_pages_with_no_items() {
        long totalItems = 0;
        int itemsPerPage = 10;
        int expectedPages = 0;
        int actualPages = Pagination.calculateTotalPages(totalItems, itemsPerPage);
        assertEquals(expectedPages, actualPages);
    }

    @Test
    public void test_calculate_total_pages_with_one_page() {
        long totalItems = 10;
        int itemsPerPage = 10;
        int expectedPages = 1;
        int actualPages = Pagination.calculateTotalPages(totalItems, itemsPerPage);
        assertEquals(expectedPages, actualPages);
    }

    @Test
    public void test_calculate_total_pages_with_items_per_page_one() {
        long totalItems = 10;
        int itemsPerPage = 1;
        int expectedPages = 10;
        int actualPages = Pagination.calculateTotalPages(totalItems, itemsPerPage);
        assertEquals(expectedPages, actualPages);
    }

    @Test
    public void test_calculate_total_pages_with_zero_items_per_page() {
        long totalItems = 10;
        int itemsPerPage = 0;
        assertThrows(IllegalArgumentException.class, () -> Pagination.calculateTotalPages(totalItems, itemsPerPage));
    }

    @Test
    public void test_calculate_current_item_count_with_full_page() {
        int pageIndex = 2;
        int itemsPerPage = 10;
        long totalItems = 30;
        int expectedItemCount = 10;
        int actualItemCount = Pagination.calculateCurrentItemCount(pageIndex, itemsPerPage, totalItems);
        assertEquals(expectedItemCount, actualItemCount);
    }

    @Test
    public void test_calculate_current_item_count_with_partial_page() {
        int pageIndex = 3;
        int itemsPerPage = 10;
        long totalItems = 25;
        int expectedItemCount = 5;
        int actualItemCount = Pagination.calculateCurrentItemCount(pageIndex, itemsPerPage, totalItems);
        assertEquals(expectedItemCount, actualItemCount);
    }

    @Test
    public void test_calculate_current_item_count_with_first_page() {
        int pageIndex = 1;
        int itemsPerPage = 10;
        long totalItems = 30;
        int expectedItemCount = 10;
        int actualItemCount = Pagination.calculateCurrentItemCount(pageIndex, itemsPerPage, totalItems);
        assertEquals(expectedItemCount, actualItemCount);
    }

    @Test
    public void test_calculate_current_item_count_with_last_page() {
        int pageIndex = 4;
        int itemsPerPage = 10;
        long totalItems = 35;
        int expectedItemCount = 5;
        int actualItemCount = Pagination.calculateCurrentItemCount(pageIndex, itemsPerPage, totalItems);
        assertEquals(expectedItemCount, actualItemCount);
    }

    @Test
    public void test_calculate_current_item_count_with_no_items() {
        int pageIndex = 1;
        int itemsPerPage = 10;
        long totalItems = 0;
        int expectedItemCount = 0;
        int actualItemCount = Pagination.calculateCurrentItemCount(pageIndex, itemsPerPage, totalItems);
        assertEquals(expectedItemCount, actualItemCount);
    }

    @Test
    public void test_calculate_current_item_count_with_invalid_page_index() {
        int pageIndex = -1;
        int itemsPerPage = 10;
        long totalItems = 30;
        int expectedItemCount = 0;
        int actualItemCount = Pagination.calculateCurrentItemCount(pageIndex, itemsPerPage, totalItems);
        assertEquals(expectedItemCount, actualItemCount);
    }

    @Test
    public void test_calculate_current_item_count_with_large_page_index() {
        int pageIndex = 10;
        int itemsPerPage = 10;
        long totalItems = 30;
        int expectedItemCount = 0;
        int actualItemCount = Pagination.calculateCurrentItemCount(pageIndex, itemsPerPage, totalItems);
        assertEquals(expectedItemCount, actualItemCount);
    }

    @Test
    public void test_calculate_offset_with_first_page() {
        int pageIndex = 1;
        int itemsPerPage = 10;
        long expectedOffset = 0;
        long actualOffset = Pagination.calculateOffset(pageIndex, itemsPerPage);
        assertEquals(expectedOffset, actualOffset);
    }

    @Test
    public void test_calculate_offset_with_second_page() {
        int pageIndex = 2;
        int itemsPerPage = 10;
        long expectedOffset = 10;
        long actualOffset = Pagination.calculateOffset(pageIndex, itemsPerPage);
        assertEquals(expectedOffset, actualOffset);
    }

    @Test
    public void test_calculate_offset_with_third_page() {
        int pageIndex = 3;
        int itemsPerPage = 10;
        long expectedOffset = 20;
        long actualOffset = Pagination.calculateOffset(pageIndex, itemsPerPage);
        assertEquals(expectedOffset, actualOffset);
    }

    @Test
    public void test_calculate_offset_with_large_page_index() {
        int pageIndex = 100;
        int itemsPerPage = 10;
        long expectedOffset = 990;
        long actualOffset = Pagination.calculateOffset(pageIndex, itemsPerPage);
        assertEquals(expectedOffset, actualOffset);
    }

    @Test
    public void test_calculate_offset_with_zero_items_per_page() {
        int pageIndex = 5;
        int itemsPerPage = 0;
        long expectedOffset = 0;
        long actualOffset = Pagination.calculateOffset(pageIndex, itemsPerPage);
        assertEquals(expectedOffset, actualOffset);
    }

    @Test
    public void test_calculate_offset_with_negative_page_index() {
        int pageIndex = -3;
        int itemsPerPage = 10;
        long expectedOffset = -40;
        long actualOffset = Pagination.calculateOffset(pageIndex, itemsPerPage);
        assertEquals(expectedOffset, actualOffset);
    }

    @Test
    public void test_of_method() {
        int pageIndex = 2;
        int itemsPerPage = 10;
        long totalItems = 25;

        Pagination pagination = Pagination.of(pageIndex, itemsPerPage, totalItems);

        assertEquals(pageIndex, pagination.pageIndex());
        assertEquals(itemsPerPage, pagination.itemsPerPage());
        assertEquals(totalItems, pagination.totalItems());
        assertEquals(3, pagination.totalPages());
        assertEquals(10, pagination.currentItemCount());
        assertEquals(itemsPerPage, pagination.limit());
        assertEquals(10, pagination.offset());
    }

}
