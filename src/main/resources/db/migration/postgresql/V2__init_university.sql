CREATE TABLE university
(
    id               UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    name             VARCHAR(255) UNIQUE             NOT NULL,
    admin_id         UUID                            NOT NULL,
    version          INTEGER                         NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE     NOT NULL,
    last_modified_at TIMESTAMP WITHOUT TIME ZONE     NOT NULL
);