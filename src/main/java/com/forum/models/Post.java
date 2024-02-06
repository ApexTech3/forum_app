package com.forum.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;
import java.util.Objects;
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
    @Column(name = "likes")
    private int likes;
    @Column(name = "dislikes")
    private int dislikes;
    @Column(name = "content")
    private String content;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    @Column(name = "archived")
    private boolean isArchived;
    @OneToMany(mappedBy = "parentPost", fetch = FetchType.EAGER)
    private Set<Comment> replies;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "posts_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "stamp_created")
    private Timestamp stampCreated;

    public Post() {
    }

    public Post(int id, String title, int likes, int dislikes, String content,
                User createdBy, boolean isArchived, Set<Comment> replies, Set<Tag> tags, Timestamp localDate) {
        this.id = id;
        this.title = title;
        this.likes = likes;
        this.dislikes = dislikes;
        this.content = content;
        this.createdBy = createdBy;
        this.isArchived = isArchived;
        this.replies = replies;
        this.tags = tags;
        this.stampCreated = localDate;
    }

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

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public Set<Comment> getReplies() {
        return replies;
    }

    public void setReplies(Set<Comment> replies) {
        this.replies = replies;
    }

    public Set<Tag> getTags() { return tags; }

    public void setTags(Set<Tag> tags) { this.tags = tags; }

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
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
