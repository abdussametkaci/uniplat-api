CREATE TABLE user_university
(
    id               UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    user_id          UUID                        NOT NULL,
    university_id    UUID                        NOT NULL,
    version          BIGINT                      NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (user_id, university_id)
);
