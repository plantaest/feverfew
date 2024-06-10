package io.github.plantaest.composite;

import io.github.plantaest.composite.helper.WikiSite;

import java.util.List;

public record WikisConfig(
        String userAgent,
        List<WikiSite> wikis
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userAgent;
        private List<WikiSite> wikis;

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder wikis(List<WikiSite> wikis) {
            this.wikis = wikis;
            return this;
        }

        public WikisConfig build() {
            return new WikisConfig(userAgent, wikis);
        }
    }

}
