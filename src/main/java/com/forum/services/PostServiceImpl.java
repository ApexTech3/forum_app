package com.forum.services;

import com.forum.exceptions.AuthorizationException;
import com.forum.helpers.PostMapper;
import com.forum.models.Post;
import com.forum.models.User;
import com.forum.models.dtos.PostResponseDto;
import com.forum.repositories.contracts.PostRepository;
import com.forum.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repository;
    private final PostMapper mapper;

    @Autowired
    public PostServiceImpl(PostRepository repository, PostMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Post> getAll() {
        return repository.getAll();
    }

    @Override
    public List<PostResponseDto> getAllDto() {
        return mapper.fromPostListToResponseDto(repository.getAll());
    }

    @Override
    public Post get(int id) {
        return repository.get(id);
    }
    @Override
    public PostResponseDto getDto(int id) {
        List<Post> posts = new ArrayList<>();
        posts.add(repository.get(id));
        return mapper.fromPostListToResponseDto(posts).get(0);
    }

    @Override
    public List<Post> getByUserId(int userId) {
        return repository.getByUserId(userId);
    }
    @Override
    public List<PostResponseDto> getByUserIdDto(int userId) {
        return mapper.fromPostListToResponseDto(repository.getByUserId(userId));
    }

    @Override
    public Post create(Post post) {
        return repository.create(post);
    }

    @Override
    public Post update(Post post, User user) {
        if (!user.isAdmin() && user.getId() != post.getCreatedBy().getId()) {
            throw new AuthorizationException("Only admins or creators can edit a post.");
        }
        return repository.update(post);
    }


    @Override
    public void archive(int id, User user) {
        if (!user.isAdmin() && user.getId() != get(id).getCreatedBy().getId()) {
            throw new AuthorizationException("Only admins or creators can delete a post.");
        }
        repository.archive(id);
    }

    @Override
    public List<Post> filter() {
        return null;
        //TODO
    }
}
