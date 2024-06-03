package io.github.plantaest.composite;

public record WikisConfig(
        String userAgent
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userAgent;

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public WikisConfig build() {
            return new WikisConfig(userAgent);
        }
    }

}
