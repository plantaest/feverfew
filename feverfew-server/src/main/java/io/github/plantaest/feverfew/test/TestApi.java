package io.github.plantaest.feverfew.test;

import io.github.plantaest.feverfew.helper.ExternalLink;
import io.github.plantaest.feverfew.helper.LinkHelper;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Path("/_test")
public class TestApi {

    @Inject
    LinkHelper linkHelper;

    @Path("/extract-external-links/{pageTitle:.*}")
    @GET
    @Operation(hidden = true)
    public List<ExternalLink> testExtractExternalLinks(@PathParam("pageTitle") String pageTitle) {
        // Ref: https://www.useragentstring.com/Firefox99.0_id_19978.php
        final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:99.0) Gecko/20100101 Firefox/99.0";
        final String encodedArticleTitle = URLEncoder.encode(pageTitle, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .header("User-Agent", userAgent)
                .uri(URI.create(MessageFormat
                        .format("https://en.wikipedia.org/w/rest.php/v1/page/{0}/html", encodedArticleTitle)))
                .GET()
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        CompletableFuture<List<ExternalLink>> response = httpClient
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(linkHelper::extractExternalLinks);

        return response.join();
    }

}
