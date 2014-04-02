# Tasks schema
 
# --- !Ups

CREATE SEQUENCE task_id_seq;
CREATE TABLE task (
    id integer NOT NULL DEFAULT nextval('task_id_seq'),
    label varchar(255),
    zombieOutbreakLocotion varchar(255),
    numberZombiesSpotted integer, 
    needHelp boolean
);

CREATE SEQUENCE user_id_seq;
CREATE TABLE "users" (
	id INTEGER NOT NULL DEFAULT nextval('user_id_seq'),
	created TIMESTAMP NOT NULL DEFAULT current_timestamp,
	last_active TIMESTAMP,
	last_login TIMESTAMP,
	user_name VARCHAR(45) UNIQUE,
	email VARCHAR(60),
	password VARCHAR(75),
	being VARCHAR(15),
	PRIMARY KEY(id)
);

CREATE TABLE "temp" (
	name varchar(35),
	age integer
);


 
# --- !Downs

 
DROP TABLE task;
DROP SEQUENCE task_id_seq;

DROP TABLE users CASCADE;
DROP TABLE user CASCADE;

DROP SEQUENCE user_id_seq CASCADE;
