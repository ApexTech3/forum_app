package com.forum.repositories.contracts;

import com.forum.models.Post;
import com.forum.models.filters.PostFilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository {

    List<Post> getAll();
    public Page<Post> findAll(int page, int size);
    List<Post> get(PostFilterOptions filterOptions);
    long getCount();
    List<Post> getMostCommented();
    List<Post> getMostLiked();
    List<Post> getRecentlyCreated();
    Post get(int id);
    Post get(String title);
    List<Post> getByUserId(int userId);
    List<Post> getBySimilarTitle(String sentence);
    Post getByTitle(String title);
    List<Post> getByContent(String sentence);
    Post create(Post post);
    Post update(Post post);
    void archive(int id);
    Post likeDislike(int userId, int postId, String likeDislike);



}