CREATE TABLE post
(
    id               UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    img_id           UUID,
    description      VARCHAR(255),
    like_count       INTEGER                     NOT NULL,
    shared_post_id   UUID,
    version          INTEGER                     NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY (id)
);
