package com.forum.repositories.contracts;

import com.forum.models.Post;

import java.util.List;

public interface PostRepository {

    List<Post> getAll();
    Post get(int id);

    List<Post> getByUserId(int userId);

    Post create(Post post);

    Post update(Post post);
    void archive(int id);

}