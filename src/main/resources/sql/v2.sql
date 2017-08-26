-- Sequence: public.database_connection_seq

-- DROP SEQUENCE public.database_connection_seq;

CREATE SEQUENCE public.database_connection_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.database_connection_seq
  OWNER TO postgres;

-- Table: public.database_connection

-- DROP TABLE public.database_connection;

CREATE TABLE public.database_connection
(
  id integer NOT NULL DEFAULT nextval('database_connection_seq'::regclass),
  user_name character varying(50),
  db_name character varying(50),
  CONSTRAINT database_connection_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.database_connection
  OWNER TO postgres;
-- Constraint: public.database_connection_pk

-- ALTER TABLE public.database_connection DROP CONSTRAINT database_connection_pk;

ALTER TABLE public.database_connection
  ADD CONSTRAINT database_connection_pk PRIMARY KEY(id);

-- Column: database_connection_id

-- ALTER TABLE public.user_actions DROP COLUMN database_connection_id;

ALTER TABLE public.user_actions ADD COLUMN database_connection_id integer;

ALTER TABLE public.user_actions
  ADD CONSTRAINT user_actions_database_connection_fk FOREIGN KEY (database_connection_id) REFERENCES public.database_connection (id)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_user_actions_database_connection_fk
  ON public.user_actions(database_connection_id);

--merge data

INSERT INTO database_connection(id, user_name, db_name)
SELECT nextval('database_connection_seq'::regclass) AS id, subquery.user_name AS user_name, subquery.db_name AS db_name
FROM (SELECT DISTINCT user_name, db_name FROM user_actions) subquery

--drop unused columns

ALTER TABLE public.user_actions DROP COLUMN db_name;
ALTER TABLE public.user_actions DROP COLUMN user_name;