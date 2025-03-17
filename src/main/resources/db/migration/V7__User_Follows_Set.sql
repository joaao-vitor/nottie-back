ALTER TABLE user_follow_user
    ADD CONSTRAINT pk_user_follow_user PRIMARY KEY (user_followed_id, user_id);

ALTER TABLE user_follow_workstation
    ADD CONSTRAINT pk_user_follow_workstation PRIMARY KEY (user_id, workstation_id);