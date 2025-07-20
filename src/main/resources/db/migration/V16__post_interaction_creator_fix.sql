ALTER TABLE post_interaction
    DROP CONSTRAINT fk_post_interaction_on_user;

ALTER TABLE post_interaction
    ADD creator_id BIGINT;

ALTER TABLE post_interaction
    ADD CONSTRAINT FK_POST_INTERACTION_ON_CREATOR FOREIGN KEY (creator_id) REFERENCES users (id);

ALTER TABLE post_interaction
    DROP COLUMN user_id;