ALTER TABLE note
    ADD notes_group_id BIGINT;

ALTER TABLE note
    ADD CONSTRAINT FK_NOTE_ON_NOTESGROUP FOREIGN KEY (notes_group_id) REFERENCES notes_group (id);