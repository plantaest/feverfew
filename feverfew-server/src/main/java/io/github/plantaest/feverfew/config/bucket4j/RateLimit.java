package io.github.plantaest.feverfew.config.bucket4j;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@InterceptorBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    String DEFAULT_BUCKET = "DEFAULT_BUCKET";

    @Nonbinding
    String bucket() default DEFAULT_BUCKET;
}
