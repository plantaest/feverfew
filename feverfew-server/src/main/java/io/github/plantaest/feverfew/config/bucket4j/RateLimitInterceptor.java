package io.github.plantaest.feverfew.config.bucket4j;

import io.github.bucket4j.Bucket;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.lang.reflect.Method;

@RateLimit
@Interceptor
@Priority(Interceptor.Priority.LIBRARY_BEFORE)
public class RateLimitInterceptor {

    @Inject
    Buckets buckets;

    @AroundInvoke
    public Object around(InvocationContext context) throws Exception {
        Method method = context.getMethod();
        String bucketName;

        if (method.isAnnotationPresent(RateLimit.class)) {
            bucketName = method.getAnnotation(RateLimit.class).bucket();
        } else if (context.getTarget().getClass().isAnnotationPresent(RateLimit.class)) {
            bucketName = context.getTarget().getClass().getAnnotation(RateLimit.class).bucket();
        } else {
            bucketName = RateLimit.DEFAULT_BUCKET;
        }

        Bucket bucket = buckets.getBucket(bucketName);
        long nanoWaitTime = bucket.tryConsumeAndReturnRemaining(1).getNanosToWaitForRefill();

        if (nanoWaitTime == 0) {
            return context.proceed();
        } else {
            throw new RateLimitException(nanoWaitTime);
        }
    }

}
