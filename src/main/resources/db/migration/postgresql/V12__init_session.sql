CREATE TABLE session
(
    id               UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    user_id          UUID                        NOT NULL,
    jti              UUID                        NOT NULL,
    version          BIGINT                      NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (user_id)
);