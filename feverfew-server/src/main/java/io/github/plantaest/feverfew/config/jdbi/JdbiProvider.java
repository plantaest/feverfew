package io.github.plantaest.feverfew.config.jdbi;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jdbi.v3.core.Jdbi;

public class JdbiProvider {

    @Inject
    AgroalDataSource defaultDataSource;

    @Singleton
    @Produces
    public Jdbi jdbi() {
        Jdbi jdbi = Jdbi.create(defaultDataSource);
        jdbi.registerRowMapper(new RecordAndAnnotatedConstructorMapper());
        return jdbi;
    }

}
