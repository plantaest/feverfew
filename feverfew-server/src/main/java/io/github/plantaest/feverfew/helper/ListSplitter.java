package io.github.plantaest.feverfew.helper;

import java.util.ArrayList;
import java.util.List;

public class ListSplitter {
    public static List<List<String>> splitList(List<String> originalList, int maxSize) {
        List<List<String>> splitList = new ArrayList<>();

        for (int i = 0; i < originalList.size(); i += maxSize) {
            List<String> sublist = originalList.subList(i, Math.min(i + maxSize, originalList.size()));
            splitList.add(new ArrayList<>(sublist));
        }

        return splitList;
    }
}
