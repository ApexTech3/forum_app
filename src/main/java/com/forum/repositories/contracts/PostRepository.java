package com.forum.repositories.contracts;

import com.forum.models.Post;
import com.forum.models.User;
import com.forum.models.dtos.PostResponseDto;
import com.forum.models.filters.PostFilterOptions;

import java.util.List;

public interface PostRepository {

    List<Post> getAll();
    List<Post> get(PostFilterOptions filterOptions);
    Post get(int id);
    Post get(String title);
    List<Post> getByUserId(int userId);
    List<Post> getByTitle(String sentence);
    List<Post> getByContent(String sentence);
    Post create(Post post);
    Post update(Post post);
    void archive(int id);
    void like(int user_id, int post_id);
    void dislike(int user_id, int post_id);
    boolean userLikedPost(int user_id, int post_id);
    boolean userDislikedPost(int user_id, int post_id);

}