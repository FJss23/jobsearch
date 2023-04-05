-- https://www.postgresql.org/docs/9.1/datatype-character.html
-- https://wiki.postgresql.org/wiki/Don't_Do_This
-- https://community.spiceworks.com/topic/2454825-zone-of-misunderstanding

-- Create a custom schema
CREATE SCHEMA IF NOT EXISTS jobsearch;

-- Tables
CREATE TABLE IF NOT EXISTS jobsearch.tag(
	tag_id integer GENERATED BY DEFAULT AS IDENTITY,
    name text NOT NULL CHECK (name <> ''),

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,

    PRIMARY KEY (tag_id)
);

CREATE TYPE job_state AS ENUM ('CREATED', 'EXPIRED', 'CLOSED');

CREATE TABLE IF NOT EXISTS jobsearch.job(
	job_id integer GENERATED BY DEFAULT AS IDENTITY,
	title text NOT NULL CHECK (title <> ''),
	role text,
	location text,
	salary_from numeric CHECK (salary_from > 0),
	salary_up_to numeric CHECK (salary_up_to > 0),
	salary_currency text,
	workday text NOT NULL CHECK (workday <> ''),
	description text NOT NULL CHECK (description <> ''),
    state job_state NOT NULL,
    work_model text NOT NULL CHECK (work_model <> ''),
    company_name text,
    company_logo_url text,
    scrapped_from_url text,

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,

    PRIMARY KEY (job_id),
    CHECK (salary_up_to >= salary_from)
);

CREATE TABLE IF NOT EXISTS jobsearch.job_tag(
    job_id integer,
    tag_id integer,

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,

    PRIMARY KEY (job_id, tag_id),
    FOREIGN KEY (tag_id) REFERENCES jobsearch.tag (tag_id) ON DELETE SET NULL (tag_id),
    FOREIGN KEY (job_id) REFERENCES jobsearch.job (job_id) ON DELETE CASCADE
);

CREATE TYPE appuser_role AS ENUM ('CANDIDATE', 'APP_ADMIN');

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

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,

    PRIMARY KEY (appuser_id)
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

CREATE TABLE IF NOT EXISTS jobsearch.refresh_token_info(
    refresh_token_info_id integer GENERATED BY DEFAULT AS IDENTITY,
    location text,
    device text,
    appuser_email text NOT NULL CHECK (appuser_email <> ''),

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,

    PRIMARY KEY (refresh_token_info_id),
    FOREIGN KEY (appuser_email) REFERENCES jobsearch.appuser (email)
);


CREATE TABLE IF NOT EXISTS jobsearch.email_sent(
    email_sent_id integer GENERATED BY DEFAULT AS IDENTITY,
    source text NOT NULL CHECK (source <> ''),
    destination text NOT NULL CHECK (destination <> ''),
    event_type text NOT NULL,
    message_id text NOT NULL,

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,

    PRIMARY KEY (email_sent_id)
);

CREATE TABLE IF NOT EXISTS jobsearch.job_deleted(
	job_id integer GENERATED BY DEFAULT AS IDENTITY,
	title text NOT NULL CHECK (title <> ''),
	role text,
	location text,
	salary_from numeric CHECK (salary_from > 0),
	salary_up_to numeric CHECK (salary_up_to > 0),
	salary_currency text,
	workday text NOT NULL CHECK (workday <> ''),
	description text NOT NULL CHECK (description <> ''),
    state job_state NOT NULL,
    work_model text NOT NULL CHECK (work_model <> ''),
    company_name text,
    scrapped_from_url text,

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,

    PRIMARY KEY (job_id),
    CHECK (salary_up_to >= salary_from)
);

CREATE TABLE IF NOT EXISTS jobsearch.job_tag_deleted(
    job_id integer,
    tag_id integer,

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,
    deleted_at timestamptz,

    PRIMARY KEY (job_id, tag_id),
    FOREIGN KEY (tag_id) REFERENCES jobsearch.tag (tag_id) ON DELETE SET NULL (tag_id),
    FOREIGN KEY (job_id) REFERENCES jobsearch.job (job_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS jobsearch.appuser_deleted(
    appuser_id integer GENERATED BY DEFAULT AS IDENTITY,
    first_name text NOT NULL CHECK (first_name <> ''),
    last_name text NOT NULL CHECK (last_name <> ''),
    email text UNIQUE NOT NULL CHECK (email <> '') ,
    password text NOT NULL CHECK (password <> ''),
    role appuser_role NOT NULL,
    locked boolean DEFAULT FALSE,
    enabled boolean DEFAULT FALSE,
    logged_at timestamptz,

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,
    deleted_at timestamptz,

    PRIMARY KEY (appuser_id)
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

CREATE TRIGGER job_updated_at_automatic
    BEFORE UPDATE ON jobsearch.job
    FOR EACH ROW
    EXECUTE FUNCTION update_value_updated_at();

CREATE TRIGGER job_tag_updated_at_automatic
    BEFORE UPDATE ON jobsearch.job_tag
    FOR EACH ROW
    EXECUTE FUNCTION update_value_updated_at();

CREATE TRIGGER appuser_updated_at_automatic
    BEFORE UPDATE ON jobsearch.appuser
    FOR EACH ROW
    EXECUTE FUNCTION update_value_updated_at();

CREATE TRIGGER refresh_token_info_updated_at_automatic
    BEFORE UPDATE ON jobsearch.refresh_token_info
    FOR EACH ROW
    EXECUTE FUNCTION update_value_updated_at();

CREATE TRIGGER email_sent_updated_at_automatic
    BEFORE UPDATE ON jobsearch.email_sent
    FOR EACH ROW
    EXECUTE FUNCTION update_value_updated_at();

CREATE FUNCTION insert_after_delete_job() RETURNS TRIGGER AS $$
    BEGIN
        INSERT
        INTO jobsearch.job_deleted(
            name,
            title,
            role,
            salary_from,
            salary_up_to,
            salary_currency,
            location,
            workday_type,
            descrpition,
            state,
            work_model,
            company_name,
            scrapped_from_url)
        VALUES(
            NEW.title,
            NEW.role,
            NEW.salary_from,
            NEW.salary_up_to,
            NEW.salary_currency,
            NEW.location,
            NEW.workday_type,
            NEW.description,
            NEW.state,
            NEW.work_model,
            NEW.company_name,
            NEW.scrapped_from_url);

        RETURN NEW;
    END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER copy_deleted_job
    AFTER DELETE ON jobsearch.job
    FOR EACH ROW
    EXECUTE FUNCTION insert_after_delete_job();


INSERT INTO jobsearch.tag(name)
VALUES ('.NET'),
    ('AI'),
    ('ASP.NET Core'),
    ('ASP.NET'),
    ('AWS'),
    ('Android'),
    ('Angular'),
    ('Apache Kafka'),
    ('Apache Spark'),
    ('Assembly'),
    ('Bash'),
    ('Blazor'),
    ('C#'),
    ('C'),
    ('C++'),
    ('CMake'),
    ('CSS'),
    ('Capacitor'),
    ('Cassandra'),
    ('Clojure'),
    ('Cobol'),
    ('Cordoba'),
    ('CouchDB'),
    ('Couchbase'),
    ('Crystal'),
    ('Dart'),
    ('Database'),
    ('Delphi'),
    ('Deno'),
    ('Django'),
    ('Docker'),
    ('Drupal'),
    ('DynamoDB'),
    ('Elasticsearch'),
    ('Electron'),
    ('Elixir'),
    ('Erlang'),
    ('Express'),
    ('F#'),
    ('FastAPI'),
    ('Figma'),
    ('Firebase'),
    ('Flutter'),
    ('Fortran'),
    ('Fullstack'),
    ('GTK'),
    ('Gatbsy'),
    ('Go'),
    ('Golang'),
    ('Godot'),
    ('Google Cloud'),
    ('Gradle'),
    ('Grafana'),
    ('Groovy'),
    ('HTML'),
    ('Hadoop'),
    ('Haskell'),
    ('Heroku'),
    ('Kubernetes'),
    ('K8s'),
    ('Hibernate'),
    ('IBM DB2'),
    ('Ionic'),
    ('Java'),
    ('JavaScript'),
    ('Jenkings'),
    ('Julia'),
    ('LISP'),
    ('Laravel'),
    ('Liferay'),
    ('Lisp'),
    ('Lua'),
    ('MATLAB'),
    ('Machine Learning'),
    ('MariaDB'),
    ('Maven'),
    ('Microsoft Azure'),
    ('Microsoft SQL Server'),
    ('MongoDB'),
    ('MySQL'),
    ('Neo4j'),
    ('NestJS'),
    ('NextJS'),
    ('NodeJS'),
    ('NumPy'),
    ('NuxtJS'),
    ('OCaml'),
    ('Objective-C'),
    ('Oracle'),
    ('PHP'),
    ('Pandas'),
    ('Pandas'),
    ('Perl'),
    ('Phoenix'),
    ('PostgreSQL'),
    ('Postgres'),
    ('Powershell'),
    ('Prometheus'),
    ('PyTorch'),
    ('Python'),
    ('Qt'),
    ('R'),
    ('React Native'),
    ('React Query'),
    ('React'),
    ('Redis'),
    ('Redux'),
    ('RoR'),
    ('Ruby'),
    ('Rust'),
    ('SASS'),
    ('SQL'),
    ('SQLite'),
    ('Scala'),
    ('Selenium'),
    ('Shopify'),
    ('Sinatra'),
    ('Solidity'),
    ('Spring Boot'),
    ('Spring'),
    ('Struts'),
    ('Svelte'),
    ('Swift'),
    ('Symfony'),
    ('TensorFlow'),
    ('Terraform'),
    ('TypeScript'),
    ('Unity 3D'),
    ('Unreal Engine'),
    ('VBA'),
    ('Vue'),
    ('Wordpress'),
    ('Kotlin'),
    ('RabbitMQ'),
    ('Xamarin'),
    ('iOS'),
    ('Debian'),
    ('Nginx'),
    ('IoT'),
    ('jQuery');

CREATE TABLE IF NOT EXISTS jobsearch.keyword_location(
    location_id integer GENERATED BY DEFAULT AS IDENTITY,
    name text,

    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    created_by text,
    updated_at timestamptz,
    updated_by text,

    PRIMARY KEY (location_id)
);

INSERT INTO jobsearch.keyword_location(name)
VALUES ('EU'),
    ('Europe'),
    ('Worldwide'),
    ('Austria'),
    ('Vienna'),
    ('Belgium'),
    ('Brussels'),
    ('Bulgaria'),
    ('Sofia'),
    ('Cyprus'),
    ('Czech Republic'),
    ('Prague'),
    ('Denmark'),
    ('Copenhagen'),
    ('Estonia'),
    ('Finland'),
    ('Helsinki'),
    ('France'),
    ('Paris'),
    ('Germany'),
    ('Berlin'),
    ('Greece'),
    ('Athens'),
    ('Hungary'),
    ('Budapest'),
    ('Ireland'),
    ('Dublin'),
    ('Italy'),
    ('Rome'),
    ('Latvia'),
    ('Riga'),
    ('Lithuania'),
    ('Vilnius'),
    ('Luxembourg'),
    ('Malta'),
    ('Valletta'),
    ('Netherlands'),
    ('Amsterdam'),
    ('Poland'),
    ('Warsaw'),
    ('Portugal'),
    ('Lisbon'),
    ('Romania'),
    ('Bucharest'),
    ('Slovakia'),
    ('Bratislava'),
    ('Slovenia'),
    ('Ljubljana'),
    ('Spain'),
    ('Madrid'),
    ('Barcelona'),
    ('Sweden'),
    ('Stockholm'),
    ('Croatia'),
    ('Zagreb'),
    ('UK'),
    ('England'),
    ('London');

-- Software Engineer
-- Backend Engineer
-- Backend  Developer
-- Backend Technical Lead
-- Frontend Engineer
-- Frontend Developer
-- Frontend Technical Lead
-- Fullstack Engineer
-- Fullstack Developer
-- Accessibility Developer
-- Cloud Developer
-- iOS Developer
-- Android Developer
-- UI Designer
-- Site Reliability Engineer
-- Product Manager
-- Data Scientist
-- System Engineer
-- Principal Engineer
-- DevOps Engineer
-- Founding Engineer
