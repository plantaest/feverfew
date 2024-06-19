package io.github.plantaest.feverfew.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.io.IOException;

@Singleton
public class ObjectMappers {

    private ObjectMapper nonUseAnnotationsMapper;
    private ObjectMapper numericBooleanMapper;

    @Inject
    ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        nonUseAnnotationsMapper = JsonMapper.builder(objectMapper.getFactory())
                .configure(MapperFeature.USE_ANNOTATIONS, false)
                .build();

        numericBooleanMapper = JsonMapper.builder(objectMapper.getFactory())
                .addModule(new SimpleModule()
                        .addSerializer(boolean.class, new BooleanToIntegerSerializer()))
                .build();
    }

    public ObjectMapper getNonUseAnnotationsMapper() {
        return nonUseAnnotationsMapper;
    }

    public ObjectMapper getNumericBooleanMapper() {
        return numericBooleanMapper;
    }

    static class BooleanToIntegerSerializer extends JsonSerializer<Boolean> {
        @Override
        public void serialize(Boolean value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeNumber(value ? 1 : 0);
        }
    }

}
