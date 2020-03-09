DELETE FROM user_roles;
DELETE FROM users;

ALTER SEQUENCE global_seq RESTART WITH 10000;

/*
 *  Encrypted passwords(bcrypt algorithm):
 *  admin --> $2a$10$RJ9mC91.xvrUHuBbHtDf2OdKw6oDijzvxNegZCoP/EaeKIk2tNR/S
 *  user2 --> $2a$10$/mfDFnJ7RKWchCfBWvhekOgPpB4WUG7KhRzHj5vMFmVrSgyuGWH42
 *  user3 --> $2a$10$/mfDFnJ7RKWchCfBWvhekOgPpB4WUG7KhRzHj5vMFmVrSgyuGWH42
 */

INSERT INTO users (username, email, password) VALUES
('Admin', 'admin@gmail.com', '$2a$10$RJ9mC91.xvrUHuBbHtDf2OdKw6oDijzvxNegZCoP/EaeKIk2tNR/S'),
('User2', 'user2@gmail.com', '$2a$10$/mfDFnJ7RKWchCfBWvhekOgPpB4WUG7KhRzHj5vMFmVrSgyuGWH42'),
('User3', 'user3@gmail.com', '$2a$10$4OjwnMBW8J4115SjI2l08eAf226Sb3IiMWdJh28fs3Uwg1F1sHjmK');

INSERT INTO user_roles (user_id, role) VALUES
(10000, 'ROLE_ADMIN'),
(10001, 'ROLE_USER'),
(10002, 'ROLE_USER');
