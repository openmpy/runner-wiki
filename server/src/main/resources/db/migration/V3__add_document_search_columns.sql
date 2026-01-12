CREATE
EXTENSION IF NOT EXISTS pg_trgm;

ALTER TABLE document
    ADD COLUMN IF NOT EXISTS title_chosung text;

ALTER TABLE document
DROP
COLUMN IF EXISTS title_norm;

ALTER TABLE document
    ADD COLUMN title_norm text
        GENERATED ALWAYS AS (lower(title)) STORED;
