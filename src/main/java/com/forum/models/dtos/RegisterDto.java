package com.forum.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterDto {

    @Size(min = 4, max = 32, message = "Username should be between 4 and 32 symbols")
    private String username;
    @Size(min = 4, max = 32, message = "Password should be between 4 and 32 symbols")
    private String password;
    @NotEmpty(message = "Password confirmation can't be empty")
    private String passwordConfirmation;
    @NotEmpty(message = "First Name can't be empty")
    private String firstName;
    @NotEmpty(message = "Last Name can't be empty")
    private String lastName;
    @Email
    @NotEmpty(message = "Email can't be empty")
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
