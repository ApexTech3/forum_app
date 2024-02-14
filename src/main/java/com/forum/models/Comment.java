package com.forum.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "comments")
public class Comment {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "comment_id")
    private int commentId;
    @Column(name = "content")
    private String content;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post parentPost;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "stamp_created")
    private Timestamp stampCreated;



    public Comment() {
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
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

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Post getParentPost() {
        return parentPost;
    }

    public void setPostId(Post parentPost) {
        this.parentPost = parentPost;
    }

    public Timestamp getStampCreated() {
        return stampCreated;
    }

    public void setStampCreated(Timestamp stampCreated) {
        this.stampCreated = stampCreated;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return getCommentId() == comment.getCommentId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommentId());
    }
}
