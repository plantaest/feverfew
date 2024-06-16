package io.github.plantaest.feverfew.config.lambda;

import io.github.plantaest.feverfew.config.AppConfig;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class LambdaFunctionStates {

    private final Map<String, LambdaFunctionState> functionStates = new ConcurrentHashMap<>();

    @Inject
    AppConfig appConfig;

    @PostConstruct
    public void init() {
        List<String> functionNames = new ArrayList<>();

        for (var entry : appConfig.lambda().functionNames().entrySet()) {
            functionNames.addAll(entry.getValue());
        }

        for (var functionName : functionNames) {
            functionStates.put(functionName, LambdaFunctionStateBuilder.builder()
                    .invocation(new AtomicInteger())
                    .isColdStarting(new AtomicBoolean())
                    .semaphore(new Semaphore(appConfig.lambda().maxConsecutiveInvocations()))
                    .latch(new AtomicReference<>(new CountDownLatch(1)))
                    .build());
        }
    }

    public LambdaFunctionState get(String functionName) {
        return functionStates.get(functionName);
    }

}
