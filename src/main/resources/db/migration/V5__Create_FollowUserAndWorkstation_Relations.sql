ALTER TABLE user_follow
    DROP CONSTRAINT fk_usefol_on_user;

ALTER TABLE user_follow
    DROP CONSTRAINT fk_usefol_on_user_followed;

CREATE TABLE user_follow_user
(
    user_followed_id BIGINT NOT NULL,
    user_id          BIGINT NOT NULL
);

CREATE TABLE user_follow_workstation
(
    user_id        BIGINT NOT NULL,
    workstation_id BIGINT NOT NULL
);

ALTER TABLE workstation
    ADD CONSTRAINT uc_workstation_username UNIQUE (username);

ALTER TABLE user_follow_user
    ADD CONSTRAINT fk_usefoluse_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_follow_user
    ADD CONSTRAINT fk_usefoluse_on_user_followed FOREIGN KEY (user_followed_id) REFERENCES users (id);

ALTER TABLE user_follow_workstation
    ADD CONSTRAINT fk_usefolwor_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_follow_workstation
    ADD CONSTRAINT fk_usefolwor_on_workstation FOREIGN KEY (workstation_id) REFERENCES workstation (id);

DROP TABLE user_follow CASCADE;