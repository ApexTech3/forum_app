package com.forum.helpers;

import com.forum.models.Post;
import com.forum.models.User;
import com.forum.models.dtos.PostRequestDto;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public Post fromRequestDto(PostRequestDto postRequestDto, User creator) {
        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setCreatedBy(creator);
        return post;
    }
}
