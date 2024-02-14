package com.forum.models.filters;

import java.util.Optional;
import java.util.function.Predicate;

public class UserFilterOptions {
    private Optional<String> username;
    private Optional<String> email;
    private Optional<String> firstName;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public UserFilterOptions() {
    }

    public UserFilterOptions(String username, String email, String firstName, String sortBy, String sortOrder) {
        this.username = Optional.ofNullable(username).filter(Predicate.not(String::isEmpty));
        this.email = Optional.ofNullable(email).filter(Predicate.not(String::isEmpty));
        this.firstName = Optional.ofNullable(firstName).filter(Predicate.not(String::isEmpty));
        this.sortBy = Optional.ofNullable(sortBy).filter(Predicate.not(String::isEmpty));
        this.sortOrder = Optional.ofNullable(sortOrder).filter(Predicate.not(String::isEmpty));
    }

    public Optional<String> getUsername() {
        return username;
    }

    public void setUsername(Optional<String> username) {
        this.username = username;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(Optional<String> email) {
        this.email = email;
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public void setFirstName(Optional<String> firstName) {
        this.firstName = firstName;
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
