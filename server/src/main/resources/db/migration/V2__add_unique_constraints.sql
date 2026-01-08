-- document(title, category)
DO
$$
    BEGIN
        IF to_regclass('public.uk_document_title_category_active') IS NULL THEN
            CREATE UNIQUE INDEX uk_document_title_category_active
                ON document (title, category)
                WHERE deleted_at IS NULL;
        END IF;
    END
$$;

-- document_history(document_id, version)
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'uk_document_history_document_version') THEN
            ALTER TABLE document_history
                ADD CONSTRAINT uk_document_history_document_version UNIQUE (document_id, version);
        END IF;
    END
$$;
