package io.github.plantaest.composite;

public record WikiConfig(
        String userAgent,
        String wikiId,
        String serverName
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userAgent;
        private String wikiId;
        private String serverName;

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder wikiId(String wikiId) {
            this.wikiId = wikiId;
            return this;
        }

        public Builder serverName(String serverName) {
            this.serverName = serverName;
            return this;
        }

        public WikiConfig build() {
            return new WikiConfig(userAgent, wikiId, serverName);
        }
    }

}
