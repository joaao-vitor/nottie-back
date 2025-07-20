ALTER TABLE note
ALTER COLUMN content
TYPE BYTEA
USING lo_get(content);