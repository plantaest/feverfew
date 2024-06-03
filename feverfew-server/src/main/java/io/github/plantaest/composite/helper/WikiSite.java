package io.github.plantaest.composite.helper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record WikiSite(
        String id,
        String serverName
) {

    public static class Wikimedia {
        public static final Map<String, WikiSite> wikis = Map.ofEntries(
                Map.entry("enwiki", new WikiSite("enwiki", "en.wikipedia.org")),
                Map.entry("viwiki", new WikiSite("viwiki", "vi.wikipedia.org")),
                Map.entry("metawiki", new WikiSite("metawiki", "meta.wikimedia.org")),
                Map.entry("commonswiki", new WikiSite("commonswiki", "commons.wikimedia.org"))
        );
    }

    public static class External {
        public static final Map<String, WikiSite> wikis = new ConcurrentHashMap<>();
    }

    public static List<String> getWikiIds() {
        return Stream.concat(Wikimedia.wikis.keySet().stream(), External.wikis.keySet().stream()).toList();
    }

    public static Map<String, WikiSite> getWikis() {
        return Stream.concat(Wikimedia.wikis.entrySet().stream(), External.wikis.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (first, last) -> new WikiSite(last.id(), last.serverName())
                ));
    }

}
