package com.forum.repositories;

import com.forum.models.Post;

public interface PostRepository {

    Post get(int id);

    Post get(String userName);

    Post create(Post post);

    Post update(Post post);

}