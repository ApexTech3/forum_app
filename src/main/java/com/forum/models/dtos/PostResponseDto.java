package com.forum.models.dtos;

import com.forum.models.Comment;
import com.forum.models.User;
import jakarta.persistence.Column;

import java.util.Set;

public class PostResponseDto {

    private String title;
    private int likes;
    private int dislikes;
    private String content;
    private String createdBy;
    private Set<Comment> replies;


    public PostResponseDto(String title, int likes, int dislikes, String content,
                           String createdBy, Set<Comment> replies) {
        this.title = title;
        this.likes = likes;
        this.dislikes = dislikes;
        this.content = content;
        this.createdBy = createdBy;
        this.replies = replies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Set<Comment> getReplies() {
        return replies;
    }

    public void setReplies(Set<Comment> replies) {
        this.replies = replies;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
}
