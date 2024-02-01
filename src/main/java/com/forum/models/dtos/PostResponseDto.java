package com.forum.models.dtos;

import com.forum.models.Comment;
import com.forum.models.Tag;

import java.time.LocalDate;
import java.util.Set;

public class PostResponseDto {

    private String title;
    private int likes;
    private int dislikes;
    private String content;
    private String createdBy;
    private Set<Comment> replies;
    private Set<Tag> tags;
    private LocalDate stampCreated;


    public PostResponseDto(String title, int likes, int dislikes, String content,
                           String createdBy, Set<Comment> replies, Set<Tag> tags, LocalDate stampCreated) {
        this.title = title;
        this.likes = likes;
        this.dislikes = dislikes;
        this.content = content;
        this.createdBy = createdBy;
        this.replies = replies;
        this.tags= tags;
        this.stampCreated = stampCreated;
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public LocalDate getStampCreated() {
        return stampCreated;
    }

    public void setStampCreated(LocalDate stampCreated) {
        this.stampCreated = stampCreated;
    }
}
