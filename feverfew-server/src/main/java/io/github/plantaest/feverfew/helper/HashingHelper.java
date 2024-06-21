package io.github.plantaest.feverfew.helper;

import jakarta.annotation.Nullable;

import java.util.zip.CRC32;

public class HashingHelper {

    public static long crc32Hash(@Nullable String str) {
        CRC32 crc32 = new CRC32();
        crc32.update((str != null ? str : "").getBytes());
        return crc32.getValue();
    }

}
