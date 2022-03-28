CREATE TABLE university_club
(
    id               UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    university_id    UUID                        NOT NULL,
    club_id          UUID                        NOT NULL,
    version          INTEGER                     NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (university_id, club_id)
);
