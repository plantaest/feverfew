package io.github.plantaest.feverfew.lambda;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TimeHelper {

    public static double durationInMillis(long startTime, long endTime) {
        return BigDecimal
                .valueOf((endTime - startTime) / 1_000_000.0)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static double durationInMillis(long startTime) {
        return TimeHelper.durationInMillis(startTime, System.nanoTime());
    }

}
