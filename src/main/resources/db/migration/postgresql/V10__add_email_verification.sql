ALTER TABLE "user"
    ADD COLUMN enabled BOOLEAN DEFAULT FALSE NOT NULL;

CREATE TABLE email_verification_code
(
    id               UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    user_id          UUID                        NOT NULL,
    code             VARCHAR(128)                NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (user_id, code)
);
