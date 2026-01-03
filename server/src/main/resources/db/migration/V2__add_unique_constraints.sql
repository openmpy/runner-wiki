-- document(title, category)
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1
                       FROM pg_constraint
                       WHERE conname = 'uk_document_title_category') THEN
            ALTER TABLE document
                ADD CONSTRAINT uk_document_title_category UNIQUE (title, category);
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
