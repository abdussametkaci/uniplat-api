CREATE TABLE university_user
(
    id               UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    university_id    UUID                        NOT NULL,
    user_id          UUID                        NOT NULL,
    version          INTEGER                     NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (university_id, user_id)
);
