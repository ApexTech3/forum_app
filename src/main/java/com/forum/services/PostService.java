package com.forum.services;

import com.forum.models.Post;
import com.forum.models.User;

import java.util.List;

public interface PostService {

    List<Post> getAll();

    Post get(int id);
    List<Post> getByUserId(int userId);

    Post create(Post post);

    Post update(Post post, User user);
    void delete(Post post, User user);
    List<Post> filter();

}
