CREATE SCHEMA IF NOT EXISTS rest;
DROP TABLE rest.PEOPLE;

CREATE TABLE IF NOT EXISTS rest.PEOPLE (
  id INT AUTO_INCREMENT CONSTRAINT pk_polls PRIMARY KEY,
  firstname VARCHAR2(50),
  lastname VARCHAR2(50),
  surname VARCHAR2(50),
  age INT,
);

INSERT INTO rest.PEOPLE (1, "corentin", "houdayer", "coco", 21);