drop schema if exists `forum`;

create schema `forum`;

use `forum`;

create table `users`
(
    `user_id`    int(11) auto_increment primary key,
    `username`   varchar(30) not null,
    `password`   varchar(30) not null,
    `first_name` varchar(32) not null,
    `last_name`  varchar(32) not null,
    `email`      varchar(30) not null,
    `is_blocked` tinyint(1)           default 0 not null,
    `is_deleted` tinyint(1)  NOT NULL DEFAULT 0,
    UNIQUE KEY `users_pk` (`username`),
    UNIQUE KEY `users_pk2` (`email`)
);

create table `posts`
(
    `post_id`    int(11) auto_increment primary key,
    `title`      varchar(64)          not null,
    `content`    varchar(8192)        not null,
    `likes`      int(11)    default 0 not null,
    `dislikes`   int(11)    default 0 not null,
    `created_by` int(11)              not null,
    `archived`   tinyint(1) default 0 not null,
    stamp_created timestamp default current_timestamp,
    constraint posts_users_user_id_fk_2

        foreign key (created_by) references users (user_id)
);


create table likes_dislikes
(
    like_id      int auto_increment
        primary key,
    post_id      int                      not null,
    user_id      int                      not null,
    like_dislike enum ('LIKE', 'DISLIKE') not null,
    constraint likes_posts_post_id_fk
        foreign key (post_id) references posts (post_id),
    constraint likes_users_user_id_fk
        foreign key (user_id) references users (user_id),
    constraint uc_post_id_user_id
        unique (post_id, user_id)
);
CREATE TABLE `phones`
(
    `phone_id` int(11)     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`  int(11)     NOT NULL,
    `phone`    varchar(50) NOT NULL,
    CONSTRAINT `phones_users_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
);

CREATE TABLE `pictures`
(
    `picture_id` int(11)     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`    int(11)     NOT NULL,
    `picture`    varchar(256) NOT NULL,
    CONSTRAINT `pictures_users_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
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

CREATE TABLE `roles`
(
    `role_id` int(11)     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `role`    varchar(20) NOT NULL
);

create table comments
(
    comment_id    int auto_increment
        primary key,
    content       text                                  not null,
    user_id       int                                   null,
    post_id       int                                   null,
    stamp_created timestamp default current_timestamp() not null,
    constraint FK_Comment_Post
        foreign key (post_id) references posts (post_id),
    constraint FK_Comment_User
        foreign key (user_id) references users (user_id)
)
    collate = utf8mb4_unicode_ci;

CREATE TABLE `users_roles`
(
    `users_roles_id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`        int(11) NOT NULL,
    `role_id`        int(11) NOT NULL,
    CONSTRAINT `users_roles_roles_role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
    CONSTRAINT `users_roles_users_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
);

create index tag_id
    on posts_tags (tag_id);


DELIMITER //
CREATE TRIGGER update_likes_dislikes
    AFTER INSERT
    ON likes_dislikes
    FOR EACH ROW
BEGIN
    DECLARE post_like INT;
    DECLARE post_dislike INT;

    -- Get the current like and dislike counts for the post
    SELECT likes, dislikes
    INTO post_like, post_dislike
    FROM posts
    WHERE post_id = NEW.post_id;

    -- Check if it's a like or dislike and update the appropriate counter
    IF NEW.like_dislike = 'LIKE' THEN
        -- It's a like
        UPDATE posts
        SET likes = post_like + 1
        WHERE post_id = NEW.post_id;
    ELSE
        -- It's a dislike
        UPDATE posts
        SET dislikes = post_dislike + 1
        WHERE post_id = NEW.post_id;
    END IF;
END;
//

CREATE TRIGGER update_likes_dislikes_after_update
    AFTER UPDATE
    ON likes_dislikes
    FOR EACH ROW
BEGIN
    DECLARE post_like INT;
    DECLARE post_dislike INT;

    -- Get the current like and dislike counts for the post
    SELECT likes, dislikes
    INTO post_like, post_dislike
    FROM posts
    WHERE post_id = NEW.post_id;

    -- Check if it's a like or dislike and update the appropriate counters
    IF NEW.like_dislike = 'LIKE' THEN
        -- It's a like
        UPDATE posts
        SET likes    = post_like + 1,
            dislikes = post_dislike - 1
        WHERE post_id = NEW.post_id;
    ELSE
        -- It's a dislike
        UPDATE posts
        SET dislikes = post_dislike + 1,
            likes    = post_like - 1
        WHERE post_id = NEW.post_id;
    END IF;
END;
//
DELIMITER ;