# Tasks schema
 
# --- !Ups


CREATE SEQUENCE user_id_seq;
CREATE TABLE "users" (
	id INTEGER NOT NULL DEFAULT nextval('user_id_seq'),
	created TIMESTAMP NOT NULL DEFAULT current_timestamp,
	last_active TIMESTAMP,
	last_login TIMESTAMP,
	user_name VARCHAR(45) UNIQUE,
	email VARCHAR(254),
	password VARCHAR(150),
	livingStatus VARCHAR(25),
	latitude DOUBLE PRECISION,
	longitude DOUBLE PRECISION,
	PRIMARY KEY(id)
);




 
# --- !Downs



DROP TABLE users CASCADE;
DROP SEQUENCE user_id_seq CASCADE;
