insert into users (username, password, first_name, last_name, email)
values ('pesho', '1234', 'Pesho', 'Peshov', 'pesho@peshomail.com');

insert into users (username, password, first_name, last_name, email)
values ('gosho', '4321', 'Gosho', 'Goshov', 'gosho@goshomail.com');

insert into users (username, password, first_name, last_name, email)
values ('ivan', '4321', 'Ivan', 'Ivanov', 'ivam@ivanmail.com');

insert into posts (title, content, created_by)
values ('Post1', 'Content1', 1);

insert into posts (title, content, created_by)
values ('Post2', 'Content2', 2);

insert into posts (title, content, created_by)
values ('Post3', 'Content3', 1);

insert into likes_dislikes (post_id, user_id, like_dislike)
values (1, 1, 'LIKE');

insert into likes_dislikes (post_id, user_id, like_dislike)
values (1, 2, 'LIKE');

insert into likes_dislikes (post_id, user_id, like_dislike)
values (1, 3, 'DISLIKE');

INSERT INTO comments (content, user_id, post_id)
VALUES ('test', 1, 1);
INSERT INTO comments (content, user_id, post_id)
VALUES ('comment2', 2, 1);
INSERT INTO comments (content, user_id, post_id)
VALUES ('comment3', 3, 3);


INSERT INTO forum.tags (name)
VALUES ('cars');
INSERT INTO forum.tags (name)
VALUES ('bikes');
INSERT INTO forum.tags (name)
VALUES ('spacecrafts');
INSERT INTO forum.posts_tags (post_id, tag_id)
VALUES (1, 1);
INSERT INTO forum.posts_tags (post_id, tag_id)
VALUES (2, 2);
INSERT INTO forum.posts_tags (post_id, tag_id)
VALUES (3, 1);

INSERT INTO roles (role)
VALUES ('ADMIN');
INSERT INTO roles (role)
VALUES ('USER');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1);
INSERT INTO users_roles (user_id, role_id)
VALUES (1, 2);
INSERT INTO users_roles (user_id, role_id)
VALUES (2, 2);
INSERT INTO users_roles (user_id, role_id)
VALUES (3, 2);