package com.forum.services;

import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityDuplicateException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.helpers.PostMapper;
import com.forum.models.Post;
import com.forum.models.User;
import com.forum.repositories.contracts.PostRepository;
import com.forum.services.contracts.PostService;
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
    public Post get(String title) {
        return repository.get(title);
    }

    @Override
    public List<Post> getByUserId(int userId) {
        return repository.getByUserId(userId);
    }


    @Override
    public Post create(Post post) {
        try {
            get(post.getTitle());
            throw new EntityDuplicateException("Post", "title", post.getTitle());
        } catch (EntityNotFoundException e) {
            return repository.create(post);
        }

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
