package io.github.plantaest.feverfew.helper;

import jakarta.annotation.Nullable;

import java.util.zip.CRC32;

public class HashingHelper {

    public static long hashIP(@Nullable String ip) {
        CRC32 crc32 = new CRC32();
        crc32.update((ip != null ? ip : "").getBytes());
        return crc32.getValue();
    }

}
