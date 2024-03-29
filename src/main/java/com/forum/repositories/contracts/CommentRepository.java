package com.forum.repositories.contracts;

import com.forum.models.Comment;

import java.util.List;

public interface CommentRepository {
    List<Comment> get();

    List<Comment> getByPostId(int postId);

    List<Comment> getByUserId(int userId);

    Comment get(int id);

    Comment create(Comment comment);

    Comment update(Comment comment);

    void delete(int id);
}
