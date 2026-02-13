CREATE
    EXTENSION IF NOT EXISTS citext;

-- 문서
CREATE TABLE document
(
    id             BIGSERIAL PRIMARY KEY,
    title          CITEXT       NOT NULL,
    title_chosung  CITEXT       NOT NULL,
    category       VARCHAR(255) NOT NULL,
    latest_version INT          NOT NULL,
    is_deleted     BOOLEAN      NOT NULL,
    deleted_at     TIMESTAMP,
    created_at     TIMESTAMP    NOT NULL,
    updated_at     TIMESTAMP    NOT NULL
);

-- 문서 기록
CREATE TABLE document_history
(
    id          BIGSERIAL PRIMARY KEY,
    document_id BIGINT       NOT NULL,
    author      VARCHAR(255) NOT NULL,
    content     TEXT         NOT NULL,
    version     INTEGER      NOT NULL,
    size        BIGINT       NOT NULL,
    client_ip   VARCHAR(255) NOT NULL,
    is_deleted  BOOLEAN      NOT NULL,
    deleted_at  TIMESTAMP,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,

    CONSTRAINT fk_document_history_document FOREIGN KEY (document_id) REFERENCES document (id)
);