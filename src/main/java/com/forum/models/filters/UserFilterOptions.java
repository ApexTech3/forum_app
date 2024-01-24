package com.forum.models.filters;

import java.util.Optional;

public class UserFilterOptions {
    private Optional<String> username;
    private Optional<String> email;
    private Optional<String> firstName;

    public UserFilterOptions() {
    }

    public UserFilterOptions(String username, String email) {
        this.username = Optional.ofNullable(username);
        this.email = Optional.ofNullable(email);
    }

    public UserFilterOptions(String username, String email, String firstName) {
        this(username, email);
        this.firstName = Optional.ofNullable(firstName);
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
}
