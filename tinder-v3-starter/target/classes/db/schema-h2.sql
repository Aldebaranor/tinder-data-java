DROP TABLE IF EXISTS t_user;
DROP TABLE IF EXISTS t_organization;

CREATE TABLE t_user
(
    id BIGINT,
    name VARCHAR(30),
    username VARCHAR(30),
    password VARCHAR(30),
    age INT,
    email VARCHAR(50),
    type INT,
    org_id BIGINT,
    create_at TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE t_organization
(
    id BIGINT,
    name VARCHAR(30),
    parent_id BIGINT,
    PRIMARY KEY (id)
);