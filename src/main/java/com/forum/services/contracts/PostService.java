package com.forum.services.contracts;

import com.forum.models.Post;
import com.forum.models.Tag;
import com.forum.models.User;
import com.forum.models.filters.PostFilterOptions;

import java.util.List;

public interface PostService {

    List<Post> getAll();

    long getCount();
    public List<Post> getMostCommented();
    public List<Post> getMostLiked();
    List<Post> get(PostFilterOptions filterOptions);
    Post getById(int id);
    List<Post> getByTitle(String title);
    List<Post> getByContent(String content);
    List<Post> getByUserId(int userId);
    Post create(Post post);
    Post update(Post post, User user);
    void archive(int id, User user);
    void like(User user, int post_id);
    void dislike(User user, int post_id);
    void associateTagWithPost(int postId, int tagId);
    public void dissociateTagWithPost(int postId, int tagId);

    List<Post> filter();

}
