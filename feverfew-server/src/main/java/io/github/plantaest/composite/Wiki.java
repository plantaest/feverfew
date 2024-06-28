package io.github.plantaest.composite;

import io.github.plantaest.composite.helper.WikiHelper;
import kong.unirest.core.UnirestInstance;

public class Wiki {

    private final WikiConfig config;

    private final UnirestInstance httpClient;

    private final String actionApiUri;

    private Wiki(WikiConfig config) {
        this.config = config;
        this.httpClient = WikiHelper.createHttpClient(config.userAgent());
        this.actionApiUri = WikiHelper.createActionApiUri(config.serverName());
    }

    private Wiki(WikiConfig config, UnirestInstance httpClient) {
        this.config = config;
        this.httpClient = httpClient;
        this.actionApiUri = WikiHelper.createActionApiUri(config.serverName());
    }

    public static Wiki init(WikiConfig config) {
        return new Wiki(config);
    }

    public static Wiki init(WikiConfig config, UnirestInstance httpClient) {
        return new Wiki(config, httpClient);
    }

    public WikiConfig config() {
        return config;
    }

    public UnirestInstance httpClient() {
        return httpClient;
    }

    public String actionApiUri() {
        return actionApiUri;
    }

    public Page page(String title) {
        return new Page(this, title);
    }

    public Revision revision(long revisionId) {
        return new Revision(this, revisionId);
    }

}
