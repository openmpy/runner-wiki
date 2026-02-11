-- 문서
CREATE TABLE document
(
    id             BIGSERIAL PRIMARY KEY,
    title          VARCHAR(255) NOT NULL,
    title_chosung  VARCHAR(255) NOT NULL,
    category       VARCHAR(50)  NOT NULL,
    latest_version INT          NOT NULL,
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
    version     INT          NOT NULL,
    size        BIGINT       NOT NULL,
    client_ip   VARCHAR(64)  NOT NULL,
    deleted_at  TIMESTAMP,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,

    CONSTRAINT fk_document_history_document FOREIGN KEY (document_id) REFERENCES document (id)
);

-- 문서 이미지
CREATE TABLE document_image
(
    id          BIGSERIAL PRIMARY KEY,
    document_id BIGINT,
    url         TEXT        NOT NULL,
    status      VARCHAR(20) NOT NULL,
    client_ip   VARCHAR(64) NOT NULL,
    expired_at  TIMESTAMP,
    deleted_at  TIMESTAMP,
    created_at  TIMESTAMP   NOT NULL,
    updated_at  TIMESTAMP   NOT NULL,

    CONSTRAINT fk_document_image_document FOREIGN KEY (document_id) REFERENCES document (id)
);