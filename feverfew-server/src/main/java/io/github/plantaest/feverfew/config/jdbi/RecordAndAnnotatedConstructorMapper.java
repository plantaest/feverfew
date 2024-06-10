package io.github.plantaest.feverfew.config.jdbi;

import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.mapper.RowMapperFactory;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;

import java.lang.reflect.Type;
import java.util.Optional;

public class RecordAndAnnotatedConstructorMapper implements RowMapperFactory {

    @Override
    public Optional<RowMapper<?>> build(Type type, ConfigRegistry config) {
        if ((type instanceof Class<?> clazz) && clazz.isRecord()) {
            return ConstructorMapper.factory(clazz).build(type, config);
        }

        return Optional.empty();
    }

}
