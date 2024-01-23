package com.forum.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private int id;
    @Column(name = "title")
    private String title;
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private Set<LikeDislike> likeDislikes;
    @Column(name = "content")
    private String content;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "parentPost", fetch = FetchType.EAGER)
    private Set<Comment> replies;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<LikeDislike> getLikeDislikes() {
        return likeDislikes;
    }

    public void setLikeDislikes(Set<LikeDislike> likeDislikes) {
        this.likeDislikes = likeDislikes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Set<Comment> getReplies() {
        return replies;
    }

    public void setReplies(Set<Comment> replies) {
        this.replies = replies;
    }


}
