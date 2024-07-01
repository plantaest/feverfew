package io.github.plantaest.feverfew.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeHelperTests {

    @Test
    public void test_duration_in_millis_with_given_start_and_end_time() {
        long startTime = System.nanoTime();
        long endTime = startTime + 1_000_000_000; // Adding 1 second in nanoseconds
        double expectedDuration = 1000.0; // 1000 milliseconds = 1 second
        double actualDuration = TimeHelper.durationInMillis(startTime, endTime);
        assertEquals(expectedDuration, actualDuration, 0.01); // Allowable error margin of 0.01
    }

    @Test
    public void test_duration_in_millis_with_only_start_time() {
        long startTime = System.nanoTime();
        double actualDuration = TimeHelper.durationInMillis(startTime);
        // Expecting a duration around 0 milliseconds, depending on system performance
        assertEquals(0.0, actualDuration, 10.0); // Allowable error margin of 10 milliseconds
    }

    @Test
    public void test_duration_in_millis_with_large_time_difference() {
        long startTime = System.nanoTime();
        long endTime = startTime + 10_000_000_000L; // Adding 10 seconds in nanoseconds
        double expectedDuration = 10000.0; // 10000 milliseconds = 10 seconds
        double actualDuration = TimeHelper.durationInMillis(startTime, endTime);
        assertEquals(expectedDuration, actualDuration, 0.01); // Allowable error margin of 0.01
    }

}
