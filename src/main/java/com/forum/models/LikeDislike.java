package com.forum.models;

import com.forum.models.enums.LikeDislikeEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "likes_dislikes")
public class LikeDislike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(name = "like_dislike")
    private LikeDislikeEnum likeOrDislike;

    public LikeDislike(Post post, User user, LikeDislikeEnum likeOrDislike) {
        this.post = post;
        this.user = user;
        this.likeOrDislike = likeOrDislike;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LikeDislikeEnum getLikeOrDislike() {
        return likeOrDislike;
    }

    public void setLikeOrDislike(LikeDislikeEnum likeOrDislike) {
        this.likeOrDislike = likeOrDislike;
    }
}
