SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: auth; Type: SCHEMA; Schema: -; Owner: discloud
--

CREATE SCHEMA auth;


ALTER SCHEMA auth OWNER TO discloud;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: app; Type: TABLE; Schema: auth; Owner: discloud
--

CREATE TABLE auth.app (
    id integer NOT NULL,
    name character varying(255),
    secret character varying(255),
    token_permission integer,
    token_type integer
);


ALTER TABLE auth.app OWNER TO discloud;

--
-- Name: app_sequence; Type: SEQUENCE; Schema: auth; Owner: discloud
--

CREATE SEQUENCE auth.app_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE auth.app_sequence OWNER TO discloud;

--
-- Name: user; Type: TABLE; Schema: auth; Owner: discloud
--

CREATE TABLE auth."user" (
    id bigint NOT NULL,
    password character varying(255),
    salt character varying(255),
    username character varying(255)
);


ALTER TABLE auth."user" OWNER TO discloud;

--
-- Data for Name: app; Type: TABLE DATA; Schema: auth; Owner: discloud
--

COPY auth.app (id, name, secret, token_permission, token_type) FROM stdin;
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: auth; Owner: discloud
--

COPY auth."user" (id, password, salt, username) FROM stdin;
\.


--
-- Name: app_sequence; Type: SEQUENCE SET; Schema: auth; Owner: discloud
--

SELECT pg_catalog.setval('auth.app_sequence', 1, false);


--
-- Name: app app_pkey; Type: CONSTRAINT; Schema: auth; Owner: discloud
--

ALTER TABLE ONLY auth.app
    ADD CONSTRAINT app_pkey PRIMARY KEY (id);


--
-- Name: app app_unique_name; Type: CONSTRAINT; Schema: auth; Owner: discloud
--

ALTER TABLE ONLY auth.app
    ADD CONSTRAINT app_unique_name UNIQUE (name);


--
-- Name: user user_unique_username; Type: CONSTRAINT; Schema: auth; Owner: discloud
--

ALTER TABLE ONLY auth."user"
    ADD CONSTRAINT user_unique_username UNIQUE (username);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: auth; Owner: discloud
--

ALTER TABLE ONLY auth."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);
