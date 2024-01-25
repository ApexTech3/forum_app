drop schema if exists `forum`;

create schema `forum`;

use `forum`;

create table `users`
(
    `user_id`         int(11) auto_increment primary key,
    `username`        varchar(30)           not null,
    `password`        varchar(30)           not null,
    `first_name`      varchar(32)           not null,
    `last_name`       varchar(32)           not null,
    `email`           varchar(30)           not null,
    `phone`           varchar(20) default null,
    `profile_picture` varchar(50) default null,
    `is_admin`        bool        default false,
    `is_blocked`      tinyint(1)  default 0 not null,
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

create table comments
(
    comment_id        int auto_increment
        primary key,
    content           text                                  not null,
    created_date_time timestamp default current_timestamp() null,
    user_id           int                                   null,
    post_id           int                                   null,
    constraint FK_Comment_Post
        foreign key (post_id) references posts (post_id),
    constraint FK_Comment_User
        foreign key (user_id) references users (user_id)
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