CREATE EXTENSION IF NOT EXISTS pg_trgm;

ALTER TABLE "user"
    ADD COLUMN full_text TSVECTOR;

UPDATE "user"
SET full_text = to_tsvector('simple', name || ' ' || surname || ' ' || coalesce(description, ''));

CREATE INDEX user_text_search_idx ON "user" USING GIN (full_text);

ALTER TABLE university
    ADD COLUMN full_text TSVECTOR;

UPDATE university
SET full_text = to_tsvector('simple', name || ' ' || coalesce(description, ''));

CREATE INDEX university_text_search_idx ON university USING GIN (full_text);

ALTER TABLE club
    ADD COLUMN full_text TSVECTOR;

UPDATE club
SET full_text = to_tsvector('simple', name || ' ' || coalesce(description, ''));

CREATE INDEX club_text_search_idx ON club USING GIN (full_text);

ALTER TABLE post
    ADD COLUMN full_text TSVECTOR;

UPDATE post
SET full_text = to_tsvector('simple', coalesce(description, '') || ' ' || coalesce(activity_title, '') || ' ' || coalesce(activity_location_description, ''));

CREATE INDEX post_text_search_idx ON post USING GIN (full_text);

-- functions
-- user
CREATE OR REPLACE FUNCTION
    func_user_full_text() RETURNS trigger AS
$user$
BEGIN
    UPDATE "user" SET full_text = to_tsvector('simple', name || ' ' || surname || ' ' || coalesce(description, '')) WHERE id = new.id;
    RETURN new;
END;
$user$ LANGUAGE plpgsql;

-- university
CREATE OR REPLACE FUNCTION
    func_university_full_text() RETURNS trigger AS
$university$
BEGIN
    UPDATE university SET full_text = to_tsvector('simple', name || ' ' || coalesce(description, '')) WHERE id = new.id;
    RETURN new;
END;
$university$ LANGUAGE plpgsql;

-- club
CREATE OR REPLACE FUNCTION
    func_club_full_text() RETURNS trigger AS
$club$
BEGIN
    UPDATE club SET full_text = to_tsvector('simple', name || ' ' || coalesce(description, '')) WHERE id = new.id;
    RETURN new;
END;
$club$ LANGUAGE plpgsql;

-- post
CREATE OR REPLACE FUNCTION
    func_post_full_text() RETURNS trigger AS
$post$
BEGIN
    UPDATE post
    SET full_text = to_tsvector('simple',
                                coalesce(description, '') || ' ' || coalesce(activity_title, '') || ' ' || coalesce(activity_location_description, ''))
    WHERE id = new.id;
    RETURN new;
END;
$post$ LANGUAGE plpgsql;

-- trigger for insert
-- user
CREATE TRIGGER trg_user_insert_full_text
    AFTER INSERT
    ON "user"
    FOR EACH ROW
EXECUTE PROCEDURE func_user_full_text();

-- university
CREATE TRIGGER trg_university_insert_full_text
    AFTER INSERT
    ON university
    FOR EACH ROW
EXECUTE PROCEDURE func_university_full_text();

-- club
CREATE TRIGGER trg_club_insert_full_text
    AFTER INSERT
    ON club
    FOR EACH ROW
EXECUTE PROCEDURE func_club_full_text();

-- post
CREATE TRIGGER trg_post_insert_full_text
    AFTER INSERT
    ON post
    FOR EACH ROW
EXECUTE PROCEDURE func_post_full_text();

-- trigger for update
-- user
CREATE TRIGGER trg_user_update_full_text
    AFTER UPDATE of name, surname, description
    ON "user"
    FOR EACH ROW
EXECUTE PROCEDURE func_user_full_text();

-- university
CREATE TRIGGER trg_university_update_full_text
    AFTER UPDATE of name, description
    ON university
    FOR EACH ROW
EXECUTE PROCEDURE func_university_full_text();

-- club
CREATE TRIGGER trg_club_update_full_text
    AFTER UPDATE of name, description
    ON club
    FOR EACH ROW
EXECUTE PROCEDURE func_club_full_text();

-- post
CREATE TRIGGER trg_post_update_full_text
    AFTER UPDATE of description, activity_title, activity_location_description
    ON post
    FOR EACH ROW
EXECUTE PROCEDURE func_post_full_text();
