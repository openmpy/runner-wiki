CREATE
EXTENSION IF NOT EXISTS pg_trgm;

ALTER TABLE document
    ADD COLUMN IF NOT EXISTS title_chosung text;

ALTER TABLE document
    ADD COLUMN IF NOT EXISTS title_norm text;

UPDATE document
SET title_norm = lower(title)
WHERE title_norm IS NULL;
