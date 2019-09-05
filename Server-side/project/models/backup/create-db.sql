-- Some assumptions:
-- We assume there is an empty DB that is called REMDB
--   and an admin user called remadmin.
-- all the *.sql files (which creates the db) will run using remadmin
-- we create a ammuser - that will later on be used to insert data without superuser privilages.

-- the next line is very important for the pgb usage, since it  
-- might keep the session open and the next create-workflows.sql etc. might have 
-- the old search path
SET search_path = cart_scheme, pg_catalog;

-- @@@@@ password should be taken from env variable
CREATE ROLE smartcartuser WITH LOGIN PASSWORD '123456';

DROP SCHEMA IF EXISTS cart_scheme CASCADE;
CREATE SCHEMA cart_scheme;
ALTER SCHEMA cart_scheme OWNER TO smartcartuser;

-- default search path for user fmsuser
ALTER ROLE smartcartuser SET search_path TO cart_scheme, pg_catalog;