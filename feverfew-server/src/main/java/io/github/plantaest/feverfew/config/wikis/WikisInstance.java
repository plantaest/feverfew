package io.github.plantaest.feverfew.config.wikis;

import io.github.plantaest.composite.Wikis;
import io.github.plantaest.composite.WikisConfig;
import jakarta.inject.Singleton;

public class WikisInstance {

    @Singleton
    public Wikis wikis() {
        WikisConfig wikisConfig = WikisConfig.builder()
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:99.0) Gecko/20100101 Firefox/99.0")
                .build();

        return Wikis.init(wikisConfig);
    }

}
