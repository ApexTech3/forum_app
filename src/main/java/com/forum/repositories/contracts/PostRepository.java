package com.forum.repositories.contracts;

import com.forum.models.Post;
import com.forum.models.dtos.PostResponseDto;

import java.util.List;

public interface PostRepository {

    List<PostResponseDto> getAll();
    PostResponseDto get(int id);

    List<PostResponseDto> getByUserId(int userId);

    Post create(Post post);

    Post update(Post post);
    void archive(int id);

}