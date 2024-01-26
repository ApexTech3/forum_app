package com.forum.helpers;

import com.forum.models.Post;
import com.forum.models.User;
import com.forum.models.dtos.PostRequestDto;
import com.forum.models.dtos.PostResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostMapper {

    public Post fromRequestDto(PostRequestDto postRequestDto, User creator) {
        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setCreatedBy(creator);
        return post;
    }

    public List<PostResponseDto> fromPostListToResponseDto(List<Post> list) {
        List<PostResponseDto> dtos = new ArrayList<>();
        for (Post post : list) {
            PostResponseDto newDto = new PostResponseDto(post.getTitle(), post.getLikes(), post.getDislikes(),
                    post.getContent(), post.getCreatedBy(), post.getReplies());
            dtos.add(newDto);
        }
        return dtos;
    }
}
