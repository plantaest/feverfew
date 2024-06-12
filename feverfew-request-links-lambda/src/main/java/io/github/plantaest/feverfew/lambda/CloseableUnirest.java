package io.github.plantaest.feverfew.lambda;

import kong.unirest.core.Config;
import kong.unirest.core.UnirestInstance;

public class CloseableUnirest extends UnirestInstance implements AutoCloseable {

    public CloseableUnirest(Config config) {
        super(config);
    }

    public static CloseableUnirest spawnInstance() {
        return new CloseableUnirest(new Config());
    }

    @Override
    public void close() {
        this.reset(true);
    }

}
