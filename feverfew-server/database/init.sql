CREATE DATABASE IF NOT EXISTS `feverfew` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `feverfew`;

CREATE TABLE IF NOT EXISTS `check`
(
    id                    BIGINT       NOT NULL,
    created_at            DATETIME     NOT NULL,
    created_by            INT UNSIGNED NOT NULL,
    wiki_id               VARCHAR(20)  NOT NULL,
    page_title            VARCHAR(255) NOT NULL,
    page_revision_id      BIGINT       NOT NULL,
    duration_in_millis    DOUBLE       NOT NULL,
    total_links           SMALLINT     NOT NULL,
    total_ignored_links   SMALLINT     NOT NULL,
    total_success_links   SMALLINT     NOT NULL,
    total_error_links     SMALLINT     NOT NULL,
    total_working_links   SMALLINT     NOT NULL,
    total_broken_links    SMALLINT     NOT NULL,
    result_schema_version TINYINT      NOT NULL,
    results               MEDIUMBLOB   NULL,
    CONSTRAINT pk_check PRIMARY KEY (id)
)
