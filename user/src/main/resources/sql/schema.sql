--
-- PostgreSQL database dump
--

-- Dumped from database version 10.4
-- Dumped by pg_dump version 10.4

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
-- Name: user_sql; Type: SCHEMA; Schema: -; Owner: discloud
--

CREATE SCHEMA user_sql;


ALTER SCHEMA user_sql OWNER TO discloud;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: client; Type: TABLE; Schema: user_sql; Owner: discloud
--

CREATE TABLE user_sql.client (
    id bigint NOT NULL,
    balance numeric(19,2),
    city character varying(255),
    email character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    postal_code character varying(255),
    state character varying(255),
    street_address character varying(255),
    country_key character varying(255)
);


ALTER TABLE user_sql.client OWNER TO discloud;

--
-- Name: client_sequence; Type: SEQUENCE; Schema: user_sql; Owner: discloud
--

CREATE SEQUENCE user_sql.client_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE user_sql.client_sequence OWNER TO discloud;

--
-- Name: client_users; Type: TABLE; Schema: user_sql; Owner: discloud
--

CREATE TABLE user_sql.client_users (
    client_id bigint NOT NULL,
    users_id bigint NOT NULL
);


ALTER TABLE user_sql.client_users OWNER TO discloud;

--
-- Name: country; Type: TABLE; Schema: user_sql; Owner: discloud
--

CREATE TABLE user_sql.country (
    key character varying(2) NOT NULL,
    flag character varying(255),
    name character varying(255)
);


ALTER TABLE user_sql.country OWNER TO discloud;

--
-- Name: payment; Type: TABLE; Schema: user_sql; Owner: discloud
--

CREATE TABLE user_sql.payment (
    id bigint NOT NULL,
    amount numeric(19,2) NOT NULL,
    currency character varying(255) NOT NULL,
    date timestamp without time zone NOT NULL,
    idempotence_key character varying(255) NOT NULL,
    paid boolean NOT NULL,
    payment_system_id character varying(255) NOT NULL,
    client_id bigint NOT NULL
);


ALTER TABLE user_sql.payment OWNER TO discloud;

--
-- Name: payment_sequence; Type: SEQUENCE; Schema: user_sql; Owner: discloud
--

CREATE SEQUENCE user_sql.payment_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE user_sql.payment_sequence OWNER TO discloud;

--
-- Name: user; Type: TABLE; Schema: user_sql; Owner: discloud
--

CREATE TABLE user_sql."user" (
    id bigint NOT NULL,
    email character varying(255),
    phone character varying(255),
    privileges character varying(255) NOT NULL,
    quota bigint,
    signup_message character varying(255),
    username character varying(255) NOT NULL
);


ALTER TABLE user_sql."user" OWNER TO discloud;

--
-- Data for Name: client; Type: TABLE DATA; Schema: user_sql; Owner: discloud
--

COPY user_sql.client (id, balance, city, email, first_name, last_name, postal_code, state, street_address, country_key) FROM stdin;
\.


--
-- Data for Name: client_users; Type: TABLE DATA; Schema: user_sql; Owner: discloud
--

COPY user_sql.client_users (client_id, users_id) FROM stdin;
\.


--
-- Data for Name: country; Type: TABLE DATA; Schema: user_sql; Owner: discloud
--

COPY user_sql.country (key, flag, name) FROM stdin;
\.


--
-- Data for Name: payment; Type: TABLE DATA; Schema: user_sql; Owner: discloud
--

COPY user_sql.payment (id, amount, currency, date, idempotence_key, paid, payment_system_id, client_id) FROM stdin;
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: user_sql; Owner: discloud
--

COPY user_sql."user" (id, email, phone, privileges, quota, signup_message, username) FROM stdin;
\.


--
-- Name: client_sequence; Type: SEQUENCE SET; Schema: user_sql; Owner: discloud
--

SELECT pg_catalog.setval('user_sql.client_sequence', 1, false);


--
-- Name: payment_sequence; Type: SEQUENCE SET; Schema: user_sql; Owner: discloud
--

SELECT pg_catalog.setval('user_sql.payment_sequence', 1, false);


--
-- Name: client client_pkey; Type: CONSTRAINT; Schema: user_sql; Owner: discloud
--

ALTER TABLE ONLY user_sql.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (id);


--
-- Name: client_users client_users_pkey; Type: CONSTRAINT; Schema: user_sql; Owner: discloud
--

ALTER TABLE ONLY user_sql.client_users
    ADD CONSTRAINT client_users_pkey PRIMARY KEY (client_id, users_id);


--
-- Name: country country_pkey; Type: CONSTRAINT; Schema: user_sql; Owner: discloud
--

ALTER TABLE ONLY user_sql.country
    ADD CONSTRAINT country_pkey PRIMARY KEY (key);


--
-- Name: payment payment_pkey; Type: CONSTRAINT; Schema: user_sql; Owner: discloud
--

ALTER TABLE ONLY user_sql.payment
    ADD CONSTRAINT payment_pkey PRIMARY KEY (id);


--
-- Name: user uk_589idila9li6a4arw1t8ht1gx; Type: CONSTRAINT; Schema: user_sql; Owner: discloud
--

ALTER TABLE ONLY user_sql."user"
    ADD CONSTRAINT uk_589idila9li6a4arw1t8ht1gx UNIQUE (phone);


--
-- Name: client_users uk_89revuv6vqo4ginb8iw0mj660; Type: CONSTRAINT; Schema: user_sql; Owner: discloud
--

ALTER TABLE ONLY user_sql.client_users
    ADD CONSTRAINT uk_89revuv6vqo4ginb8iw0mj660 UNIQUE (users_id);


--
-- Name: country uk_llidyp77h6xkeokpbmoy710d4; Type: CONSTRAINT; Schema: user_sql; Owner: discloud
--

ALTER TABLE ONLY user_sql.country
    ADD CONSTRAINT uk_llidyp77h6xkeokpbmoy710d4 UNIQUE (name);


--
-- Name: user uk_ob8kqyqqgmefl0aco34akdtpe; Type: CONSTRAINT; Schema: user_sql; Owner: discloud
--

ALTER TABLE ONLY user_sql."user"
    ADD CONSTRAINT uk_ob8kqyqqgmefl0aco34akdtpe UNIQUE (email);


--
-- Name: user uk_sb8bbouer5wak8vyiiy4pf2bx; Type: CONSTRAINT; Schema: user_sql; Owner: discloud
--

ALTER TABLE ONLY user_sql."user"
    ADD CONSTRAINT uk_sb8bbouer5wak8vyiiy4pf2bx UNIQUE (username);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: user_sql; Owner: discloud
--

ALTER TABLE ONLY user_sql."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: payment fk3fpanu7q9tijfio491vu0fd0r; Type: FK CONSTRAINT; Schema: user_sql; Owner: discloud
--

ALTER TABLE ONLY user_sql.payment
    ADD CONSTRAINT fk3fpanu7q9tijfio491vu0fd0r FOREIGN KEY (client_id) REFERENCES user_sql.client(id);


--
-- Name: client_users fk3khrtdnnrussfvwh20111ec4s; Type: FK CONSTRAINT; Schema: user_sql; Owner: discloud
--

ALTER TABLE ONLY user_sql.client_users
    ADD CONSTRAINT fk3khrtdnnrussfvwh20111ec4s FOREIGN KEY (users_id) REFERENCES user_sql."user"(id);


--
-- Name: client fk5gygg2yvi3uvubgs518kj366l; Type: FK CONSTRAINT; Schema: user_sql; Owner: discloud
--

ALTER TABLE ONLY user_sql.client
    ADD CONSTRAINT fk5gygg2yvi3uvubgs518kj366l FOREIGN KEY (country_key) REFERENCES user_sql.country(key);


--
-- Name: client_users fk9ei6cd7e10t14hx5dqafktb5w; Type: FK CONSTRAINT; Schema: user_sql; Owner: discloud
--

ALTER TABLE ONLY user_sql.client_users
    ADD CONSTRAINT fk9ei6cd7e10t14hx5dqafktb5w FOREIGN KEY (client_id) REFERENCES user_sql.client(id);


--
-- PostgreSQL database dump complete
--
