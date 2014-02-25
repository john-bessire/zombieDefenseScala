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
 
# --- !Downs
 
DROP TABLE task;
DROP SEQUENCE task_id_seq;