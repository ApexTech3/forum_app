package com.forum.repositories.contracts;

import com.forum.models.Post;
import com.forum.models.filters.PostFilterOptions;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostRepository {

    List<Post> getAll();
    public Page<Post> findAll(int page, int size);
    List<Post> get(PostFilterOptions filterOptions);
    public List<Post> getByContent(String query);
    long getCount();
    List<Post> getMostCommented();
    List<Post> getMostLiked();
    List<Post> getRecentlyCreated();
    Post get(int id);
    Post get(String title);
    List<Post> getByUserId(int userId);
    List<Post> getBySimilarTitle(String sentence);
    Post getByTitle(String title);
    List<Post> getByContentOrTitle(String sentence);
    Post create(Post post);
    Post update(Post post);
    void archive(int id);
    Post likeDislike(int userId, int postId, String likeDislike);


}