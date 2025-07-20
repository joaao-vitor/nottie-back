ALTER TABLE note
    ADD published BOOLEAN DEFAULT false;

ALTER TABLE note
    ALTER COLUMN published SET NOT NULL;