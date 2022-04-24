CREATE TABLE user_club
(
    id               UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    club_id          UUID                        NOT NULL,
    user_id          UUID                        NOT NULL,
    version          INTEGER                     NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (club_id, user_id)
);
