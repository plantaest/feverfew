package io.github.plantaest.feverfew.repository;

import io.github.plantaest.feverfew.dto.response.GetAllStatsResponse;
import io.github.plantaest.feverfew.entity.Check;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CheckRepository {

    @Inject
    Jdbi jdbi;

    public void create(Check check) {
        jdbi.useTransaction(handle -> handle
                .createUpdate("""
                        INSERT INTO `check` (
                            id,
                            created_at,
                            created_by,
                            wiki_id,
                            page_title,
                            page_revision_id,
                            duration_in_millis,
                            total_links,
                            total_ignored_links,
                            total_success_links,
                            total_error_links,
                            total_working_links,
                            total_broken_links,
                            result_schema_version,
                            results
                        ) VALUES (
                            :id,
                            :createdAt,
                            :createdBy,
                            :wikiId,
                            :pageTitle,
                            :pageRevisionId,
                            :durationInMillis,
                            :totalLinks,
                            :totalIgnoredLinks,
                            :totalSuccessLinks,
                            :totalErrorLinks,
                            :totalWorkingLinks,
                            :totalBrokenLinks,
                            :resultSchemaVersion,
                            :results
                        )
                        """)
                .bindMethods(check)
                .execute());
    }

    public Optional<Check> findById(Long id) {
        return jdbi.inTransaction(handle -> handle
                .createQuery("""
                        SELECT
                            id,
                            created_at,
                            created_by,
                            wiki_id,
                            page_title,
                            page_revision_id,
                            duration_in_millis,
                            total_links,
                            total_ignored_links,
                            total_success_links,
                            total_error_links,
                            total_working_links,
                            total_broken_links,
                            result_schema_version,
                            results
                        FROM `check`
                        WHERE id = :id
                        """)
                .bind("id", id)
                .mapTo(Check.class)
                .findOne());
    }

    public long count() {
        return jdbi.inTransaction(handle -> handle
                .createQuery("SELECT COUNT(id) FROM `check`")
                .mapTo(long.class)
                .one());
    }

    public List<Check> findAll(int limit, long offset) {
        return jdbi.inTransaction(handle -> handle
                .createQuery("""
                        SELECT
                            id,
                            created_at,
                            created_by,
                            wiki_id,
                            page_title,
                            page_revision_id,
                            duration_in_millis,
                            total_links,
                            total_ignored_links,
                            total_success_links,
                            total_error_links,
                            total_working_links,
                            total_broken_links,
                            result_schema_version
                        FROM `check`
                        ORDER BY id DESC
                        LIMIT :limit
                        OFFSET :offset
                        """)
                .bind("limit", limit)
                .bind("offset", offset)
                .mapTo(Check.class)
                .list());
    }

    public GetAllStatsResponse getAllStats() {
        return jdbi.inTransaction(handle -> handle
                .createQuery("""
                        SELECT
                            COUNT(id) AS total_checks,
                            COUNT(DISTINCT created_by) AS total_anonymous_users,
                            COUNT(DISTINCT wiki_id) AS total_wikis,
                            COUNT(DISTINCT page_title) AS total_pages,
                            SUM(duration_in_millis) AS total_duration_in_millis,
                            SUM(total_links) AS total_links,
                            SUM(total_ignored_links) AS total_ignored_links,
                            SUM(total_working_links) AS total_working_links,
                            SUM(total_broken_links) AS total_broken_links
                        FROM `check`
                        """)
                .mapTo(GetAllStatsResponse.class)
                .one());
    }

}
