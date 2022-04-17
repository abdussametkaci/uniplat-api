CREATE TYPE POST_TYPE AS ENUM (
    'FEMALE',
    'MALE'
    );

CREATE TABLE post
(
    id               UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    img_id           UUID                        NOT NULL,
    description      VARCHAR(255),
    post_type        POST_TYPE                   NOT NULL,
    like_counter     INTEGER                     NOT NULL,
    version          INTEGER                     NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY (id)
);
