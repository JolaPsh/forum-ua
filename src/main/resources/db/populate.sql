DELETE FROM user_roles;
DELETE FROM users;

ALTER SEQUENCE global_seq RESTART WITH 10000;

/*
 *  Encrypted passwords(bcrypt algorithm):
 *  user1 --> $2a$10$L1fVwsOiF5is9CQu4s.Op.QTPmeNJHcU9h6GdgvMAb6ePCGxT.yUW
 *  user2 --> $2a$10$aUafOaKlMwVgXGilS8LI4eUfY.1AhUsnTdYgjiKkZICDdrIvY.kf.
 *  user3 --> $2a$10$/mfDFnJ7RKWchCfBWvhekOgPpB4WUG7KhRzHj5vMFmVrSgyuGWH42
 */

INSERT INTO users (username, email, password) VALUES
('Admin', 'admin@gmail.com', '$2a$10$L1fVwsOiF5is9CQu4s.Op.QTPmeNJHcU9h6GdgvMAb6ePCGxT.yUW'),
('User1', 'user1@gmail.com', '$2a$10$aUafOaKlMwVgXGilS8LI4eUfY.1AhUsnTdYgjiKkZICDdrIvY.kf.'),
('User2', 'user2@gmail.com', '$2a$10$/mfDFnJ7RKWchCfBWvhekOgPpB4WUG7KhRzHj5vMFmVrSgyuGWH42');

INSERT INTO user_roles (user_id, role) VALUES
(10000, 'ROLE_ADMIN'),
(10001, 'ROLE_USER'),
(10002, 'ROLE_USER');
