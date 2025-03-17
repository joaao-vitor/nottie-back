ALTER TABLE user_details
    DROP CONSTRAINT fk_user_details_on_user;
ALTER TABLE user_details
    ADD CONSTRAINT fk_user_details_on_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE verification_token
    DROP CONSTRAINT FK_VERIFICATION_TOKEN_ON_USER;
ALTER TABLE verification_token
    ADD CONSTRAINT FK_VERIFICATION_TOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE workstation_follows_user
    DROP CONSTRAINT fk_worfoluse_on_user;
ALTER TABLE workstation_follows_user
    ADD CONSTRAINT fk_worfoluse_on_user FOREIGN KEY (user_followed_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE workstation_leader
    DROP CONSTRAINT fk_worlea_on_user;
ALTER TABLE workstation_leader
    ADD CONSTRAINT fk_worlea_on_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE workstation_member
    DROP CONSTRAINT fk_wormem_on_user;
ALTER TABLE workstation_member
    ADD CONSTRAINT fk_wormem_on_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE workstation
    DROP CONSTRAINT FK_WORKSTATION_ON_CREATOR;
ALTER TABLE workstation
    ADD CONSTRAINT FK_WORKSTATION_ON_CREATOR FOREIGN KEY (creator_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE workstation
    DROP CONSTRAINT FK_WORKSTATION_ON_UPDATED_BY;
ALTER TABLE workstation
    ADD CONSTRAINT FK_WORKSTATION_ON_UPDATED_BY FOREIGN KEY (updated_by) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE verification_token
    DROP CONSTRAINT FK_VERIFICATION_TOKEN_ON_USER;
ALTER TABLE verification_token
    ADD CONSTRAINT FK_VERIFICATION_TOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE workstation_follows_workstation
    DROP CONSTRAINT fk_worfolwor_on_workstation;
ALTER TABLE workstation_follows_workstation
    ADD CONSTRAINT fk_worfolwor_on_workstation FOREIGN KEY (workstation_id) REFERENCES workstation (id) ON DELETE CASCADE;