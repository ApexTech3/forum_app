package com.forum.repositories;

import com.forum.models.Post;

import java.util.List;

public interface PostRepository {

    Post get(int id);

    List<Post> getByUserId(int userId);

    Post create(Post post);

    Post update(Post post);

}