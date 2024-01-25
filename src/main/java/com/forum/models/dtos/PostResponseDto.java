package com.forum.models.dtos;

import com.forum.models.Comment;
import com.forum.models.LikeDislike;
import com.forum.models.User;

import java.util.Set;

public class PostResponseDto {

    private String title;
    private Set<LikeDislike> likeDislikes;
    private String content;
    private User createdBy;
    private Set<Comment> replies;

    public PostResponseDto(String title, Set<LikeDislike> likeDislikes, String content, User createdBy, Set<Comment> replies) {
        this.title = title;
        this.likeDislikes = likeDislikes;
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
