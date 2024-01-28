package com.forum.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserAdminDto implements BaseUserDto {
    private String password;
    @Size(min = 4, max = 32, message = "First Name should be between 4 and 32 symbols")
    private String firstName;
    @Size(min = 4, max = 32, message = "Last Name should be between 4 and 32 symbols")
    private String lastName;
    @Email
    private String email;
    private String phone;
    private String profilePicture;

    private boolean isAdmin;

    private boolean isBlocked;

    public UserAdminDto() {
    }

    public UserAdminDto(String password, String firstName, String lastName, String email, String phone, String profilePicture, boolean isAdmin, boolean isBlocked) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.profilePicture = profilePicture;
        this.isAdmin = isAdmin;
        this.isBlocked = isBlocked;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
