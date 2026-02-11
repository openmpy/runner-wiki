-- 문서
ALTER TABLE document
    ADD CONSTRAINT uk_document_title_category_is_deleted
        UNIQUE (title, category, is_deleted);

-- 문서 기록
ALTER TABLE document_history
    ADD CONSTRAINT uk_document_history_document_version
        UNIQUE (document_id, version);