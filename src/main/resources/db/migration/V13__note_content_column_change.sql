ALTER TABLE note
    DROP COLUMN content;

ALTER TABLE note
    ADD content OID;