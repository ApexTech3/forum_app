package com.forum.services;

import com.forum.exceptions.AuthorizationException;
import com.forum.models.Post;
import com.forum.models.User;
import com.forum.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repository;

    @Autowired
    public PostServiceImpl(PostRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Post> getAll() {
        return repository.getAll();
    }

    @Override
    public Post get(int id) {
        return repository.get(id);
    }

    @Override
    public List<Post> getByUserId(int userId) {
        return repository.getByUserId(userId);
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
        return repository.create(post);
    }


    @Override
    public void delete(Post post, User user) {
        if (!user.isAdmin() && user.getId() != post.getCreatedBy().getId()) {
            throw new AuthorizationException("Only admins or creators can delete a post.");
        }
        //repository.delete(post);
        //TODO
    }

    @Override
    public List<Post> filter() {
        return null;
        //TODO
    }
}
