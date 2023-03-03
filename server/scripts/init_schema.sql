-- https://www.postgresql.org/docs/9.1/datatype-character.html
-- https://wiki.postgresql.org/wiki/Don't_Do_This
-- https://community.spiceworks.com/topic/2454825-zone-of-misunderstanding

-- Create a custom schema
CREATE SCHEMA IF NOT EXISTS jobsearch;

-- Tables
CREATE TABLE IF NOT EXISTS jobsearch.tag(
	tag_id integer GENERATED BY DEFAULT AS IDENTITY,
    default_name text NOT NULL CHECK (default_name <> ''),
	tag_code text NOT NULL CHECK (tag_code <> ''),

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,

    PRIMARY KEY (tag_id)
);

CREATE TABLE IF NOT EXISTS jobsearch.company(
	company_id integer GENERATED BY DEFAULT AS IDENTITY,
	name text NOT NULL CHECK (name <> ''),
    description text NOT NULL CHECK (description <> ''),
	logo_url text,
    twitter text,
    facebook text,
    instagram text,
    website text,

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,
    deleted_at timestamptz,

    PRIMARY KEY (company_id)
);

CREATE TYPE joboffer_state AS ENUM ('CREATED', 'EXPIRED', 'CLOSED');

CREATE TYPE joboffer_workday AS ENUM ('FULL_TIME', 'PART_TIME', 'PER_HOUR');

CREATE TYPE workplace_system_type  AS ENUM ('REMOTE_WORK', 'HYBRID_WORK', 'ON_SITE');

CREATE TABLE IF NOT EXISTS jobsearch.joboffer(
	joboffer_id integer GENERATED BY DEFAULT AS IDENTITY,
	title text NOT NULL CHECK (title <> ''),
	industry text,
	salary_from numeric CHECK (salary_from > 0),
	salary_up_to numeric CHECK (salary_up_to > 0),
	salary_coin text,
	location text NOT NULL CHECK (location <> ''),
	workday_code joboffer_workday NOT NULL,
	description text NOT NULL CHECK (description <> ''),
    state joboffer_state NOT NULL,
	company_id integer,
    workplace_system workplace_system_type NOT NULL,
    how_to_apply text,
    scrapped boolean,

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,
    deleted_at timestamptz,

    PRIMARY KEY (joboffer_id),
    FOREIGN KEY (company_id) REFERENCES jobsearch.company (company_id) ON DELETE CASCADE,
    CHECK (salary_up_to >= salary_from)
);

CREATE TABLE IF NOT EXISTS jobsearch.joboffer_tag(
    joboffer_id integer,
    tag_id integer,

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,
    deleted_at timestamptz,

    PRIMARY KEY (joboffer_id, tag_id),
    FOREIGN KEY (tag_id) REFERENCES jobsearch.tag (tag_id) ON DELETE SET NULL (tag_id),
    FOREIGN KEY (joboffer_id) REFERENCES jobsearch.joboffer (joboffer_id) ON DELETE CASCADE
);

CREATE TYPE appuser_role AS ENUM ('CANDIDATE', 'COMPANY', 'COMPANY_ADMIN', 'APP_ADMIN');

CREATE TABLE IF NOT EXISTS jobsearch.appuser(
    appuser_id integer GENERATED BY DEFAULT AS IDENTITY,
    first_name text NOT NULL CHECK (first_name <> ''),
    last_name text NOT NULL CHECK (last_name <> ''),
    email text UNIQUE NOT NULL CHECK (email <> '') ,
    password text NOT NULL CHECK (password <> ''),
    role appuser_role NOT NULL,
    locked boolean DEFAULT FALSE,
    enabled boolean DEFAULT FALSE,
    logged_at timestamptz,
    company_id integer,

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,
    deleted_at timestamptz,

    PRIMARY KEY (appuser_id),
    FOREIGN KEY (company_id) REFERENCES jobsearch.company (company_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS jobsearch.cv(
    cv_id integer GENERATED BY DEFAULT AS IDENTITY,
    appuser_id integer,
    activities text,
    contact_email text NOT NULL CHECK (contact_email <> ''),
    phone text,

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,
    deleted_at timestamptz,

    PRIMARY KEY (cv_id),
    FOREIGN KEY (appuser_id) REFERENCES jobsearch.appuser (appuser_id) ON DELETE CASCADE
);

-- EXP = Experience / EDU = Education
CREATE TYPE career_section AS ENUM ('EXP', 'EDU');

CREATE TABLE IF NOT EXISTS jobsearch.career(
    career_id integer GENERATED BY DEFAULT AS IDENTITY,
    cv_id integer,
    organization text NOT NULL CHECK (organization <> ''),
    date_from date NOT NULL,
    date_until date,
    section career_section NOT NULL,
    description text NOT NULL CHECK (description <> ''),

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,
    deleted_at timestamptz,

    PRIMARY KEY (career_id),
    FOREIGN KEY (cv_id) REFERENCES jobsearch.cv (cv_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS jobsearch.cv_tag(
    cv_id integer,
    tag_id integer,

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,

    PRIMARY KEY (cv_id, tag_id),
    FOREIGN KEY (tag_id) REFERENCES jobsearch.tag (tag_id) ON DELETE SET NULL (tag_id),
    FOREIGN KEY (cv_id) REFERENCES jobsearch.cv (cv_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS jobsearch.selection_process(
    joboffer_id integer,
    appuser_id integer,
    company_id integer,
    discarded_by_company boolean,
    last_time_reviewed date,

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,

    PRIMARY KEY (joboffer_id, appuser_id),
    FOREIGN KEY (joboffer_id) REFERENCES jobsearch.joboffer (joboffer_id),
    FOREIGN KEY (appuser_id ) REFERENCES jobsearch.appuser (appuser_id) ON DELETE SET NULL(appuser_id),
    FOREIGN KEY (company_id) REFERENCES jobsearch.company (company_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS jobsearch.confirmation_token(
    confirmation_token_id integer GENERATED BY DEFAULT AS IDENTITY,
    token text,
    expires_at timestamptz,
    confirmed_at timestamptz,
    appuser_email text NOT NULL CHECK (appuser_email <> ''),

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (confirmation_token_id),
    FOREIGN KEY (appuser_email) REFERENCES jobsearch.appuser (email)
);

-- Triggers
CREATE FUNCTION update_value_updated_at() RETURNS TRIGGER AS $$
    BEGIN
       NEW.updated_at = now();
       RETURN NEW;
    END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tag_updated_at_automatic
    BEFORE UPDATE ON jobsearch.tag
    FOR EACH ROW
    EXECUTE FUNCTION update_value_updated_at();

CREATE TRIGGER company_updated_at_automatic
    BEFORE UPDATE ON jobsearch.company
    FOR EACH ROW
    EXECUTE FUNCTION update_value_updated_at();

CREATE TRIGGER joboffer_updated_at_automatic
    BEFORE UPDATE ON jobsearch.joboffer
    FOR EACH ROW
    EXECUTE FUNCTION update_value_updated_at();

CREATE TRIGGER joboffer_tag_updated_at_automatic
    BEFORE UPDATE ON jobsearch.joboffer_tag
    FOR EACH ROW
    EXECUTE FUNCTION update_value_updated_at();

CREATE TRIGGER appuser_updated_at_automatic
    BEFORE UPDATE ON jobsearch.appuser
    FOR EACH ROW
    EXECUTE FUNCTION update_value_updated_at();

CREATE TRIGGER cv_updated_at_automatic
    BEFORE UPDATE ON jobsearch.cv
    FOR EACH ROW
    EXECUTE FUNCTION update_value_updated_at();

CREATE TRIGGER career_updated_at_automatic
    BEFORE UPDATE ON jobsearch.career
    FOR EACH ROW
    EXECUTE FUNCTION update_value_updated_at();

CREATE TRIGGER cv_tag_updated_at_automatic
    BEFORE UPDATE ON jobsearch.cv_tag
    FOR EACH ROW
    EXECUTE FUNCTION update_value_updated_at();

CREATE TRIGGER selection_process_updated_at_automatic
    BEFORE UPDATE ON jobsearch.selection_process
    FOR EACH ROW
    EXECUTE FUNCTION update_value_updated_at();