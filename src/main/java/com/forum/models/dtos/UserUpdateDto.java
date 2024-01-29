package com.forum.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserUpdateDto implements BaseUserDto {
    private String password;
    @Size(min = 4, max = 32, message = "First Name should be between 4 and 32 symbols")
    private String firstName;
    @Size(min = 4, max = 32, message = "Last Name should be between 4 and 32 symbols")
    private String lastName;
    @Email
    private String email;

    public UserUpdateDto() {
    }

    public UserUpdateDto(String password, String firstName, String lastName, String email) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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
}
