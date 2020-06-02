DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START 10000;

CREATE TABLE users
(
    usr_id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    usr_name             CHARACTER VARYING       NOT NULL,
    usr_email            CHARACTER VARYING       UNIQUE NOT NULL,
    usr_registered       TIMESTAMP DEFAULT now() NOT NULL,
    usr_password         CHARACTER VARYING       NOT NULL,
    usr_enabled          BOOL DEFAULT TRUE       NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (usr_email);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    user_role CHARACTER VARYING,
    CONSTRAINT user_roles_idx UNIQUE (user_id, user_role),
    FOREIGN KEY (user_id) REFERENCES users (usr_id) ON DELETE CASCADE
);

ALTER TABLE users ADD COLUMN usr_status VARCHAR;