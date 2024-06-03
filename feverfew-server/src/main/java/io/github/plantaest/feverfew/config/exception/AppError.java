package io.github.plantaest.feverfew.config.exception;

import java.net.URI;
import java.util.Collections;
import java.util.List;

public record AppError(
        int status,
        URI type,
        String title,
        String detail,
        URI instance,
        String code,
        String errorId,
        List<Violation> violations
) {

    public record Violation(
            String field,
            List<String> messages
    ) {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int status;
        private URI type;
        private String title;
        private String detail;
        private URI instance;
        private String code;
        private String errorId;
        private List<Violation> violations = Collections.emptyList();

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder type(URI type) {
            this.type = type;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public Builder instance(URI instance) {
            this.instance = instance;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder errorId(String errorId) {
            this.errorId = errorId;
            return this;
        }

        public Builder violations(List<Violation> violations) {
            this.violations = violations;
            return this;
        }

        public AppError build() {
            return new AppError(status, type, title, detail, instance, code, errorId, violations);
        }
    }

}
