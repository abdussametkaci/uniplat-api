CREATE TYPE OWNER_TYPE AS ENUM (
    'USER',
    'UNIVERSITY',
    'CLUB'
    );

CREATE TYPE POST_TYPE AS ENUM (
    'POST',
    'ACTIVITY',
    'SURVEY'
    );

CREATE TABLE post
(
    id                UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    img_id            UUID,
    description       VARCHAR(255),
    owner_type        OWNER_TYPE                  NOT NULL,
    post_type         POST_TYPE                   NOT NULL,
    owner_id          UUID                        NOT NULL,
    shared_post_id    UUID,
    activity_title    VARCHAR(255),
    activity_start_at TIMESTAMP WITHOUT TIME ZONE,
    version           BIGINT                      NOT NULL,
    created_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY (id)
);
