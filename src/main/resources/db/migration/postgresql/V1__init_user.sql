CREATE TYPE GENDER AS ENUM (
    'FEMALE',
    'MALE'
    );

CREATE TYPE USER_TYPE AS ENUM (
    'STUDENT',
    'TEACHER'
    );
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE "user"
(
    id               UUID    DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
    name             VARCHAR(255)                       NOT NULL,
    surname          VARCHAR(255)                       NOT NULL,
    gender           GENDER                             NOT NULL,
    birth_date       TIMESTAMP WITHOUT TIME ZONE        NOT NULL,
    email            VARCHAR(255) UNIQUE                NOT NULL,
    password         VARCHAR(255)                       NOT NULL,
    university_id    UUID                               NOT NULL,
    type             USER_TYPE                          NOT NULL,
    description      VARCHAR(255),
    profile_img_id   UUID,
    message_accessed BOOLEAN DEFAULT TRUE,
    version          INTEGER                            NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE        NOT NULL,
    last_modified_at TIMESTAMP WITHOUT TIME ZONE        NOT NULL
);
