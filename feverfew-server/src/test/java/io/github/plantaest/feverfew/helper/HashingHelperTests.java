package io.github.plantaest.feverfew.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class HashingHelperTests {

    @Test
    public void test_crc32_hash_with_null_string() {
        String str = null;
        long expectedHash = 0L;
        long actualHash = HashingHelper.crc32Hash(str);
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void test_crc32_hash_with_empty_string() {
        String str = "";
        long expectedHash = 0L;
        long actualHash = HashingHelper.crc32Hash(str);
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void test_crc32_hash_with_non_empty_string() {
        String str = "Hello, world!";
        long expectedHash = 3957769958L;
        long actualHash = HashingHelper.crc32Hash(str);
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void test_crc32_hash_with_unicode_string() {
        String str = "😀 Unicode test!";
        long expectedHash = 3181152739L;
        long actualHash = HashingHelper.crc32Hash(str);
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void test_crc32_hash_with_different_cases() {
        String str1 = "Hello, world!";
        String str2 = "hello, world!";
        long hash1 = HashingHelper.crc32Hash(str1);
        long hash2 = HashingHelper.crc32Hash(str2);
        assertNotEquals(hash1, hash2);
    }

}
