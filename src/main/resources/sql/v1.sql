-- Database: sqlcmd_log

-- DROP DATABASE sqlcmd_log;

CREATE DATABASE sqlcmd_log
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Ukrainian_Ukraine.1251'
       LC_CTYPE = 'Ukrainian_Ukraine.1251'
       CONNECTION LIMIT = -1;

-- Schema: public

-- DROP SCHEMA public;

CREATE SCHEMA public
  AUTHORIZATION postgres;

GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;
COMMENT ON SCHEMA public
  IS 'standard public schema';

-- Sequence: public.users_action_seq

-- DROP SEQUENCE public.users_action_seq;

CREATE SEQUENCE public.users_action_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 30
  CACHE 1;
ALTER TABLE public.users_action_seq
  OWNER TO postgres;
-- Table: public.user_actions

-- DROP TABLE public.user_actions;

CREATE TABLE public.user_actions
(
  id integer NOT NULL DEFAULT nextval('users_action_seq'::regclass),
  user_name character varying(50),
  db_name character varying(50),
  action character varying(50),
  CONSTRAINT user_actions_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.user_actions
  OWNER TO postgres;
-- Constraint: public.user_actions_pkey

-- ALTER TABLE public.user_actions DROP CONSTRAINT user_actions_pkey;

ALTER TABLE public.user_actions
  ADD CONSTRAINT user_actions_pkey PRIMARY KEY(id);
