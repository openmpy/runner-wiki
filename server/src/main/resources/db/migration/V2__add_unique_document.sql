-- 문서
CREATE UNIQUE INDEX uk_document_active
    ON document (title, category) WHERE is_deleted = FALSE;

-- 문서 기록
ALTER TABLE document_history
    ADD CONSTRAINT uk_document_history_document_version
        UNIQUE (document_id, version);