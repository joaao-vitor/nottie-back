ALTER TABLE notes_group
    ADD workstation_id BIGINT;

ALTER TABLE notes_group
    ADD CONSTRAINT FK_NOTES_GROUP_ON_WORKSTATION FOREIGN KEY (workstation_id) REFERENCES workstation (id);