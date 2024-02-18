package com.forum.services.contracts;

import com.forum.models.Post;
import com.forum.models.User;
import com.forum.models.filters.PostFilterOptions;
import org.springframework.data.domain.Page;

import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.awt.print.Pageable;
import java.util.List;

public interface PostService {

    List<Post> getAll();

    long getCount();

    public Page<Post> getAllPosts(int page, int size);

    public List<Post> getMostCommented();

    public List<Post> getMostLiked();

    List<Post> getRecentlyCreated();

    List<Post> get(PostFilterOptions filterOptions);

    List<Post> getByContentOrTitle(PostFilterOptions filterOptions);

    Post getById(int id);

    List<Post> getBySimilarTitle(String title);

    List<Post> getByContent(String content);

    List<Post> getByUserId(int userId);

    Post create(Post post);

    Post update(Post post, User user);

    void archive(int id, User user);

    Post like(User user, int postId);

    Post dislike(User user, int postId);

    void associateTagWithPost(int postId, int tagId);

    public void dissociateTagWithPost(int postId, int tagId);
    List<Post> getPostsByTag(String tagName);

}
