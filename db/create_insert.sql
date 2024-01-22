drop database if exists forum;

create database forum;

use forum;

create table `roles`
(
    `role_id` int(11) auto_increment primary key,
    `name`    varchar(30) not null,
    constraint `roles_pk2` unique (`name`)
);

create table `users`
(
    `user_id`    int(11) auto_increment primary key,
    `username`   varchar(30)           not null,
    `password`   varchar(30)           not null,
    `first_name` varchar(32)           not null,
    `last_name`  varchar(32)           not null,
    `email`      varchar(30)           not null,
    `phone`      varchar(20) default null,
    `is_admin`   bool        default false,
    `is_blocked` tinyint(1)  default 0 not null,
    UNIQUE KEY `users_pk` (`username`),
    UNIQUE KEY `users_pk2` (`email`)
);

create table `posts`
(
    `post_id`    int(11) auto_increment primary key,
    `title`      varchar(64)   not null,
    `content`    varchar(8192) not null,
    `created_by` int(11)       not null
);

create table `posts_replies`
(
    `post_id`  int(11) not null,
    `reply_id` int(11) not null,
    KEY `posts_replies_posts_id_fk` (`post_id`),
    KEY `posts_replies_posts_id_fk2` (`reply_id`),
    CONSTRAINT `posts_replies_posts_id_fk` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`),
    CONSTRAINT `posts_replies_posts_id_fk2` FOREIGN KEY (`reply_id`) REFERENCES `posts` (`post_id`)
);

create table likes_dislikes
(
    like_id      int auto_increment
        primary key,
    post_id      int         not null,
    user_id      int         not null,
    like_dislike varchar(50) not null,
    constraint likes_posts_post_id_fk
        foreign key (post_id) references posts (post_id),
    constraint likes_users_user_id_fk
        foreign key (user_id) references users (user_id),
    constraint uc_post_user
        unique (post_id, user_id)
);

create table tags
(
    tag_id int auto_increment
        primary key,
    name   varchar(255) not null,
    constraint name
        unique (name)
);

create table posts_tags
(
    post_id int not null,
    tag_id  int not null,
    primary key (post_id, tag_id),
    constraint post_tag_ibfk_1
        foreign key (post_id) references posts (post_id),
    constraint post_tag_ibfk_2
        foreign key (tag_id) references tags (tag_id)
);

create index tag_id
    on posts_tags (tag_id);

insert into roles (name) value ('ADMIN');

insert into roles (name) value ('USER');

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

#
# insert into likes_dislikes (post_id, user_id, like_dislike)
# values (2, 3, 1);
