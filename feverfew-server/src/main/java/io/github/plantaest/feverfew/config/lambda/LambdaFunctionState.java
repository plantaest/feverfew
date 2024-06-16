package io.github.plantaest.feverfew.config.lambda;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Builder
public record LambdaFunctionState(
        AtomicInteger invocation,
        AtomicBoolean isColdStarting,
        Semaphore semaphore,
        AtomicReference<CountDownLatch> latch
) {}
