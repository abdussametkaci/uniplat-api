CREATE TABLE post_comment
(
    id               UUID         NOT NULL DEFAULT uuid_generate_v4(),
    user_id          UUID         NOT NULL,
    post_id          UUID         NOT NULL,
    comment          VARCHAR(255) NOT NULL,
    version          INTEGER      NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY (id)
);
