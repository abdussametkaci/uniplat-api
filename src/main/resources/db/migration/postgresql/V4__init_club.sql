CREATE TABLE club
(
    id               UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    name             VARCHAR(255)                NOT NULL,
    university_id    UUID                        NOT NULL,
    admin_id         UUID                        NOT NULL,
    version          INTEGER                     NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);
