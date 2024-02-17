package com.forum.models.dtos;

import com.forum.models.Role;
import com.forum.models.dtos.interfaces.AdminUpdate;
import com.forum.models.dtos.interfaces.Login;
import com.forum.models.dtos.interfaces.Register;
import com.forum.models.dtos.interfaces.UserUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class UserDto {
    @NotEmpty(message = "Username can't be empty", groups = {Register.class, Login.class})
    @Size(min = 4, max = 32, message = "Username should be between 4 and 32 symbols", groups = {Register.class})
    private String username;
    @NotEmpty(message = "Current Password can't be empty", groups = {UserUpdate.class})
    private String currentPassword;
    @NotEmpty(message = "Password can't be empty", groups = {Register.class, Login.class})
    @Size(min = 4, max = 32, message = "Password should be between 4 and 32 symbols", groups = {Register.class})
    private String password;
    @NotEmpty(message = "Password confirmation can't be empty", groups = {Register.class})
    @Size(min = 4, max = 32, message = "Password should be between 4 and 32 symbols", groups = {Register.class})
    private String passwordConfirmation;
    @NotEmpty(message = "First Name can't be empty", groups = {Register.class, UserUpdate.class, AdminUpdate.class})
    @Size(min = 4, max = 32, message = "First Name should be between 4 and 32 symbols", groups = {Register.class, UserUpdate.class, AdminUpdate.class})
    private String firstName;
    @Size(min = 4, max = 32, message = "Last Name should be between 4 and 32 symbols", groups = {Register.class, UserUpdate.class, AdminUpdate.class})
    @NotEmpty(message = "Last Name can't be empty", groups = {Register.class, UserUpdate.class, AdminUpdate.class})
    private String lastName;
    @Email(message = "Invalid email", groups = {Register.class, UserUpdate.class, AdminUpdate.class})
    @NotEmpty(message = "Email can't be empty", groups = {Register.class, UserUpdate.class, AdminUpdate.class})
    private String email;
    private String profilePicture;

    private String phone;
    private Set<Role> roles;
    private boolean isBlocked;
    private boolean isDeleted;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
