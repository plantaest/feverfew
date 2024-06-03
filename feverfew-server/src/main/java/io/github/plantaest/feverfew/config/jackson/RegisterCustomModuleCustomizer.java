package io.github.plantaest.feverfew.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Singleton
public class RegisterCustomModuleCustomizer implements ObjectMapperCustomizer {

    @Override
    public int priority() {
        // Ref: https://github.com/quarkusio/quarkus/issues/15018#issuecomment-778018501
        return MINIMUM_PRIORITY;
    }

    @Override
    public void customize(ObjectMapper objectMapper) {
        SimpleModule module = new SimpleModule();

        JsonSerializer<Instant> instantJsonSerializer = new JsonSerializer<>() {
            @Override
            public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException {
                final DateTimeFormatter formatter = DateTimeFormatter
                        .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                        .withZone(ZoneOffset.UTC);
                jsonGenerator.writeString(formatter.format(instant));
            }
        };

        module.addSerializer(Instant.class, instantJsonSerializer);

        objectMapper.registerModule(module);
    }

}
