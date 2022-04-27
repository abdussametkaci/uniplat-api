CREATE TABLE user_follow
(
    id               UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    user_id          UUID                        NOT NULL,
    follower_id      UUID                        NOT NULL,
    version          INTEGER                     NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (user_id, follower_id)
);
