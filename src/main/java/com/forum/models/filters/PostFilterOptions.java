package com.forum.models.filters;

import java.util.Optional;

public class PostFilterOptions {
    private Optional<String> id;
    private Optional<String> title;
    private Optional<String> content;
    private Optional<String> creator;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;


    public PostFilterOptions(Optional<String> id, Optional<String> title, Optional<String> content,
                             Optional<String> creator, Optional<String> sortBy, Optional<String> sortOrder) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creator = creator;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    public Optional<String> getId() {
        return id;
    }

    public void setId(Optional<String> id) {
        this.id = id;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public void setTitle(Optional<String> title) {
        this.title = title;
    }

    public Optional<String> getContent() {
        return content;
    }

    public void setContent(Optional<String> content) {
        this.content = content;
    }

    public Optional<String> getCreator() {
        return creator;
    }

    public void setCreator(Optional<String> creator) {
        this.creator = creator;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public void setSortBy(Optional<String> sortBy) {
        this.sortBy = sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Optional<String> sortOrder) {
        this.sortOrder = sortOrder;
    }
}
