package com.forum.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDto implements BaseUserDto {
    @NotNull(message = "Username can't be empty")
    private String username;
    @NotNull(message = "Password can't be empty")
    private String password;
    @NotNull(message = "First Name can't be empty")
    @Size(min = 4, max = 32, message = "First Name should be between 4 and 32 symbols")
    private String firstName;
    @Size(min = 4, max = 32, message = "Last Name should be between 4 and 32 symbols")
    @NotNull(message = "Last Name can't be empty")
    private String lastName;
    @Email
    @NotNull(message = "Email can't be empty")
    private String email;
    private String profilePicture;

    public UserDto(String username, String password, String firstName, String lastName, String email, String profilePicture) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
