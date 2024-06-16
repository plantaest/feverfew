package io.github.plantaest.feverfew.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MapSplitter {
    public static <K, V> List<Map<K, V>> splitMapBalanced(Map<K, V> map, int maxSize) {
        int totalSize = map.size();
        int numSubMaps = (totalSize + maxSize - 1) / maxSize;
        int baseSize = totalSize / numSubMaps;
        int remainder = totalSize % numSubMaps;

        List<Map<K, V>> subMaps = new ArrayList<>();

        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        for (int i = 0; i < numSubMaps; i++) {
            Map<K, V> subMap = new HashMap<>();
            int subMapSize = baseSize + (i < remainder ? 1 : 0);
            for (int j = 0; j < subMapSize; j++) {
                if (iterator.hasNext()) {
                    Map.Entry<K, V> entry = iterator.next();
                    subMap.put(entry.getKey(), entry.getValue());
                }
            }
            subMaps.add(subMap);
        }

        return subMaps;
    }
}
