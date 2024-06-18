package io.github.plantaest.feverfew.config.bucket4j;

import java.time.Duration;

public class RateLimitException extends RuntimeException {

    private final Duration waitTime;

    public RateLimitException(long nanoWaitTime) {
        super();
        this.waitTime = Duration.ofNanos(nanoWaitTime);
    }

    public long getWaitTimeInSeconds() {
        return waitTime.getSeconds();
    }

    @Override
    public String getMessage() {
        return "Maximum request limit reached, please try again in %s second(s) [%s]."
                .formatted(waitTime.getSeconds(), waitTime);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
