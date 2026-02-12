CREATE INDEX idx_document_active_updated_id_desc
    ON document (updated_at DESC, id DESC) WHERE is_deleted = FALSE;

CREATE INDEX idx_document_active_category_updated_id_desc
    ON document (category, updated_at DESC, id DESC) WHERE is_deleted = FALSE;

CREATE INDEX idx_document_active_title_prefix
    ON document (title) WHERE is_deleted = FALSE;

CREATE INDEX idx_document_active_title_chosung_prefix
    ON document (title_chosung) WHERE is_deleted = FALSE;

CREATE INDEX idx_document_history_active_document_version_id_desc
    ON document_history (document_id, version DESC, id DESC) WHERE is_deleted = FALSE;