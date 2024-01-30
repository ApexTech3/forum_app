package com.forum.services;

import com.forum.exceptions.AlreadyLikedDislikedException;
import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityDuplicateException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.models.Post;
import com.forum.models.Tag;
import com.forum.models.User;
import com.forum.models.filters.PostFilterOptions;
import com.forum.repositories.contracts.PostRepository;
import com.forum.repositories.contracts.TagRepository;
import com.forum.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repository;

    private final TagRepository tagRepository;

    @Autowired
    public PostServiceImpl(PostRepository repository, TagRepository tagRepository) {
        this.repository = repository;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Post> getAll() {
        return repository.getAll();
    }

    @Override
    public List<Post> get(PostFilterOptions filterOptions) {
        return repository.get(filterOptions);
    }

    @Override
    public Post getById(int id) {
        return repository.get(id);
    }

    @Override
    public List<Post> getByTitle(String title) {
        return repository.getByTitle(title);
    }

    @Override
    public List<Post> getByContent(String content) {
        return repository.getByContent(content);
    }

    @Override
    public List<Post> getByUserId(int userId) {
        return repository.getByUserId(userId);
    }


    @Override
    public Post create(Post post) {
        try {
            getByTitle(post.getTitle());
            throw new EntityDuplicateException("Post", "title", post.getTitle());
        } catch (EntityNotFoundException e) {
            return repository.create(post);
        }

    }

    @Override
    public Post update(Post post, User user) {
        if (!user.isAdmin() && user.getId() != post.getCreatedBy().getId()) {//todo check if user is blocked
            throw new AuthorizationException("Only admins or creators can edit a post.");
        }
        return repository.update(post);
    }


    @Override
    public void archive(int id, User user) {
        if (!user.isAdmin() && user.getId() != getById(id).getCreatedBy().getId()) {
            throw new AuthorizationException("Only admins or creators can delete a post.");
        }
        repository.archive(id);
    }

    @Override
    public void like(User user, int post_id) {
        if (repository.userLikedPost(user.getId(), post_id)) {
            throw new AlreadyLikedDislikedException("The post was already liked by this user.");
        } else repository.like(user.getId(), post_id);
    }

    @Override
    public void dislike(User user, int post_id) {
        if (repository.userDislikedPost(user.getId(), post_id)) {
            throw new AlreadyLikedDislikedException("The post was already disliked by this user.");
        } else repository.dislike(user.getId(), post_id);
    }

    public void associateTagWithPost(int postId, int tagId) {
        Post post = repository.get(postId);
        Tag tag = tagRepository.getById(tagId);

        post.getTags().add(tag);
        repository.update(post);
    }

    public void dissociateTagWithPost(int postId, int tagId) {
        Post post = repository.get(postId);
        Tag tag = tagRepository.getById(tagId);

        if(!post.getTags().remove(tag)) throw new EntityNotFoundException("Post", "Tag", "Not Found") ;
        repository.update(post);
    }

    @Override
    public List<Post> filter() {
        return null;
        //TODO
    }
}
