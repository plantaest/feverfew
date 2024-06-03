package io.github.plantaest.feverfew.config.tsid;

import com.github.f4b6a3.tsid.TsidFactory;
import jakarta.inject.Singleton;

import java.time.Instant;

public class TsidConfig {

    @Singleton
    public TsidFactory tsidFactory() {
        // Ref: Discord Snowflakes: https://github.com/f4b6a3/tsid-creator
        int worker = 1;
        int process = 1;
        int node = (worker << 5 | process);

        Instant customEpoch = Instant.parse("2015-01-01T00:00:00.000Z");

        return TsidFactory.builder()
                .withCustomEpoch(customEpoch)
                .withNode(node)
                .build();
    }

}
