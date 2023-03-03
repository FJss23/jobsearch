-- Create a user to be used in the app
CREATE USER jobsearch_user NOSUPERUSER NOCREATEROLE INHERIT ENCRYPTED PASSWORD 'user';
GRANT CONNECT ON DATABASE jobsearch_db TO jobsearch_user;

GRANT USAGE ON SCHEMA jobsearch TO jobsearch_user;
GRANT SELECT, INSERT, DELETE, UPDATE ON ALL TABLES IN SCHEMA jobsearch TO jobsearch_user;
