package com.forum.helpers;

import com.forum.models.Post;
import com.forum.models.User;
import com.forum.models.dtos.PostRequestDto;
import com.forum.models.dtos.PostResponseDto;
import com.forum.services.contracts.PostService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostMapper {

    private final PostService postService;

    public PostMapper(PostService postService) {
        this.postService = postService;
    }


    public Post fromRequestDto(int id, PostRequestDto postRequestDto, User creator) {
        Post post = fromRequestDto(postRequestDto, creator);
        post.setId(id);
        Post repositoryPost = postService.getById(id);
        post.setLikes(repositoryPost.getLikes());
        post.setDislikes(repositoryPost.getDislikes());
        post.setArchived(repositoryPost.isArchived());
        post.setReplies(repositoryPost.getReplies());
        post.setTags(repositoryPost.getTags());
        post.setStampCreated(repositoryPost.getStampCreated());
        return post;
    }

    public Post fromRequestDto(PostRequestDto postRequestDto, User creator) {
        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setCreatedBy(creator);
        return post;
    }

    public PostResponseDto toPostResponseDto(Post post) {
        return new PostResponseDto(post.getTitle(), post.getLikes(), post.getDislikes(),
                post.getContent(), post.getCreatedBy().getUsername(), post.getReplies(), post.getTags(), post.getStampCreated());
    }


    public List<PostResponseDto> fromPostListToResponseDto(List<Post> list) {
        List<PostResponseDto> dtos = new ArrayList<>();
        for (Post post : list) {
            dtos.add(toPostResponseDto(post));
        }
        return dtos;
    }
}
