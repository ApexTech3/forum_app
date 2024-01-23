package com.forum.services;

import com.forum.models.Comment;
import com.forum.models.User;

import java.util.List;

public interface CommentService {

    List<Comment> get();

    List<Comment> getByPostId(int postId);

    List<Comment> getByUserId(int userId);

    Comment get(int id);

    void create(Comment comment);

    void update(Comment comment, User user);

    void delete(int id, User user);
}
