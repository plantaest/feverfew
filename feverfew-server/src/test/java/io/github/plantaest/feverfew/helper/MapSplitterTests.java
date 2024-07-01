package io.github.plantaest.feverfew.helper;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapSplitterTests {

    @Test
    public void test_split_map_balanced_with_small_map() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "One");
        map.put(2, "Two");
        map.put(3, "Three");
        map.put(4, "Four");
        map.put(5, "Five");

        int maxSize = 3;

        List<Map<Integer, String>> subMaps = MapSplitter.splitMapBalanced(map, maxSize);

        assertEquals(2, subMaps.size());

        Map<Integer, String> subMap1 = subMaps.getFirst();
        assertEquals(3, subMap1.size());
        assertTrue(subMap1.containsKey(1));
        assertTrue(subMap1.containsKey(2));
        assertTrue(subMap1.containsKey(3));

        Map<Integer, String> subMap2 = subMaps.get(1);
        assertEquals(2, subMap2.size());
        assertTrue(subMap2.containsKey(4));
        assertTrue(subMap2.containsKey(5));
    }

    @Test
    public void test_split_map_balanced_with_large_map() {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 1; i <= 100; i++) {
            map.put("Key" + i, i);
        }

        int maxSize = 20;

        List<Map<String, Integer>> subMaps = MapSplitter.splitMapBalanced(map, maxSize);

        assertEquals(5, subMaps.size());

        for (int i = 0; i < 4; i++) {
            Map<String, Integer> subMap = subMaps.get(i);
            assertEquals(20, subMap.size());
        }

        Map<String, Integer> lastSubMap = subMaps.get(4);
        assertEquals(20, lastSubMap.size());
    }

    @Test
    public void test_split_map_balanced_with_max_size_larger_than_map() {
        Map<Character, String> map = new HashMap<>();
        map.put('a', "Apple");
        map.put('b', "Banana");
        map.put('c', "Cherry");

        int maxSize = 5;

        List<Map<Character, String>> subMaps = MapSplitter.splitMapBalanced(map, maxSize);

        assertEquals(1, subMaps.size());

        Map<Character, String> subMap = subMaps.getFirst();
        assertEquals(3, subMap.size());
        assertTrue(subMap.containsKey('a'));
        assertTrue(subMap.containsKey('b'));
        assertTrue(subMap.containsKey('c'));
    }

}
