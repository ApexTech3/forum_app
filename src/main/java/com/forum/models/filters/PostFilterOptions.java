package com.forum.models.filters;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class PostFilterOptions {
    private Optional<Integer> id;
    private Optional<String> title;
    private Optional<String> content;
    private Optional<Integer> creator;
    private Optional<List<Integer>> tags;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;



    public PostFilterOptions() {
    }

    public PostFilterOptions(Integer id, String title, String content,
                             Integer creator, List<Integer> tags, String sortBy, String sortOrder) {
        this.id = Optional.ofNullable(id);
        this.title = Optional.ofNullable(title);
        this.content = Optional.ofNullable(content);
        this.creator = Optional.ofNullable(creator);
        this.tags = Optional.ofNullable(tags).filter(Predicate.not(List::isEmpty));
        this.sortBy = Optional.ofNullable(sortBy).filter(Predicate.not(String::isEmpty));
        this.sortOrder = Optional.ofNullable(sortOrder).filter(Predicate.not(String::isEmpty));

    }

    public Optional<Integer> getId() {
        return id;
    }

    public void setId(Optional<Integer> id) {
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

    public Optional<Integer> getCreator() {
        return creator;
    }

    public void setCreator(Optional<Integer> creator) {
        this.creator = creator;
    }

    public Optional<List<Integer>> getTags() {return tags; }

    public void setTags(Optional<List<Integer>> tags) { this.tags = tags; }

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

    public boolean isTagsFilteringEnabled() {
        return tags.isPresent() && !tags.get().isEmpty();
    }
}
