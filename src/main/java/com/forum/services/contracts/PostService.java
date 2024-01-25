package com.forum.services.contracts;

import com.forum.models.Post;
import com.forum.models.User;
import com.forum.models.dtos.PostResponseDto;

import java.util.List;

public interface PostService {

    List<Post> getAll();
    List<PostResponseDto> getAllDto();
    Post get(int id);
    PostResponseDto getDto(int id);
    List<Post> getByUserId(int userId);
    List<PostResponseDto> getByUserIdDto(int userId);
    Post create(Post post);
    Post update(Post post, User user);
    void archive(int id, User user);
    List<Post> filter();

}
