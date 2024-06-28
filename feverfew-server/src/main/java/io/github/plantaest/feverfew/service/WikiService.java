package io.github.plantaest.feverfew.service;

import io.github.plantaest.composite.Wiki;
import io.github.plantaest.composite.Wikis;
import io.github.plantaest.composite.type.RevisionWikitextResult;
import io.github.plantaest.feverfew.dto.common.AppResponse;
import io.github.plantaest.feverfew.dto.response.GetRevisionWikitextResponse;
import io.github.plantaest.feverfew.dto.response.GetRevisionWikitextResponseBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class WikiService {

    @Inject
    Wikis wikis;

    public AppResponse<GetRevisionWikitextResponse> getRevisionWikitext(String wikiId, Long revisionId) {
        Wiki wiki = wikis.getWiki(wikiId);
        RevisionWikitextResult result = wiki.revision(revisionId).wikitext();
        GetRevisionWikitextResponse response = GetRevisionWikitextResponseBuilder.builder()
                .title(result.title())
                .pageId(result.pageId())
                .revisionId(result.revisionId())
                .wikitext(result.wikitext())
                .build();
        return AppResponse.ok(response);
    }

}
