-- flyway:executeInTransaction=false

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_document_updated_at_desc
    ON document (updated_at DESC, id)
    WHERE deleted_at IS NULL;

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_document_category_updated_at_desc
    ON document (category, updated_at DESC, id)
    WHERE deleted_at IS NULL;

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_document_history_version_desc
    ON document_history (document_id, version DESC, id)
    WHERE deleted_at IS NULL;
