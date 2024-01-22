insert into users (username, password, first_name, last_name, email, phone, is_admin)
values ('pesho', '1234', 'Pesho', 'Peshov', 'pesho@peshomail.com', '+359123123', true);

insert into users (username, password, first_name, last_name, email, is_admin)
values ('gosho', '4321', 'Gosho', 'Goshov', 'gosho@goshomail.com', false);

insert into users (username, password, first_name, last_name, email, is_admin)
values ('ivan', '4321', 'Ivan', 'Ivanov', 'ivam@ivanmail.com', false);

insert into posts (title, content, created_by)
values ('Post1', 'Content1', 1);

insert into posts (title, content, created_by)
values ('Post2', 'Content2', 2);

insert into posts (title, content, created_by)
values ('Post3', 'Content3', 1);

insert into posts_replies (post_id, reply_id)
values (1, 2);

insert into posts_replies (post_id, reply_id)
values (1, 3);

insert into likes_dislikes (post_id, user_id, like_dislike)
values (1, 1, 'LIKE');

insert into likes_dislikes (post_id, user_id, like_dislike)
values (1, 2, 'LIKE');

insert into likes_dislikes (post_id, user_id, like_dislike)
values (1, 3, 'DISLIKE');

INSERT INTO forum.comments ( content, created_date_time, user_id, post_id)
VALUES ( 'test', '2024-01-22 17:53:55', 1, 1);
INSERT INTO forum.comments ( content, created_date_time, user_id, post_id)
VALUES ( 'comment2', '2024-01-22 17:54:16', 2, 1);
INSERT INTO forum.comments ( content, created_date_time, user_id, post_id)
VALUES ( 'comment3', '2024-01-22 17:54:34', 3, 3);


#
# insert into likes_dislikes (post_id, user_id, like_dislike)
# values (2, 3, 1);
