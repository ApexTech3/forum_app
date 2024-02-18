package com.forum.services;

import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityDuplicateException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.helpers.AuthenticationHelper;
import com.forum.models.Post;
import com.forum.models.Tag;
import com.forum.models.User;
import com.forum.models.filters.PostFilterOptions;
import com.forum.repositories.contracts.PostRepository;
import com.forum.repositories.contracts.TagRepository;
import com.forum.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public Page<Post> getAllPosts(int page, int size) {
        return repository.findAll(page, size,new PostFilterOptions());//todo
    }

    public Page<Post> getAllPosts(int page, int size) {
        return repository.findAll(page, size);
    }

    @Override
    public long getCount() {
        return repository.getCount();
    }

    @Override
    public List<Post> getMostCommented() {
        return repository.getMostCommented();
    }

    @Override
    public List<Post> getMostLiked() {
        return repository.getMostLiked();
    }

    @Override
    public List<Post> getRecentlyCreated() {
        return repository.getRecentlyCreated();
    }

    @Override
    public List<Post> get(PostFilterOptions filterOptions) {
        return repository.get(filterOptions);
    }

    @Override
    public List<Post> getByContentOrTitle(PostFilterOptions filterOptions) {
        return repository.getByContentOrTitle(filterOptions);
    }

    @Override
    public Post getById(int id) {
        return repository.get(id);
    }

    @Override
    public List<Post> getBySimilarTitle(String title) {
        return repository.getBySimilarTitle(title);
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
            repository.getByTitle(post.getTitle());
            throw new EntityDuplicateException("Post", "title", post.getTitle());
        } catch (EntityNotFoundException e) {
            return repository.create(post);
        }

    }

    @Override
    public Post update(Post post, User user) {
        if (!AuthenticationHelper.isAdmin(user) && user.getId() != post.getCreatedBy().getId()) {
            throw new AuthorizationException("Only admins or creators can edit a post.");
        }
        return repository.update(post);
    }


    @Override
    public void archive(int id, User user) {
        if (!AuthenticationHelper.isAdmin(user) && user.getId() != getById(id).getCreatedBy().getId()) {
            throw new AuthorizationException("Only admins or creators can delete a post.");
        }
        repository.archive(id);
    }

    @Override
    public Post like(User user, int postId) {
        return repository.likeDislike(user.getId(), postId, "LIKE");
    }

    @Override
    public Post dislike(User user, int postId) {
        return repository.likeDislike(user.getId(), postId, "DISLIKE");
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

        if (!post.getTags().contains(tag)) throw new EntityNotFoundException("Post", "Tag ID", String.valueOf(tagId));

        post.getTags().remove(tag);

        repository.update(post);
    }

    @Override
    public List<Post> getPostsByTag(String tagName) {
        return repository.getPostsByTag(tagName);
    }
}