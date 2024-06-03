package io.github.plantaest.composite;

public record WikiConfig(
        String serverName,
        String userAgent
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String serverName;
        private String userAgent;

        public Builder serverName(String serverName) {
            this.serverName = serverName;
            return this;
        }

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public WikiConfig build() {
            return new WikiConfig(serverName, userAgent);
        }
    }

}
