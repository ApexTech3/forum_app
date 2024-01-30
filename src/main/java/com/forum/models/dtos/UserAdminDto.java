package com.forum.models.dtos;

import com.forum.models.Role;

import java.util.Set;

public class UserAdminDto extends UserUpdateDto {
    private String phone;
    private Set<Role> roles;
    private boolean isBlocked;

    public UserAdminDto() {
    }

    public UserAdminDto(String password, String firstName, String lastName, String email, String phone, String profilePicture, Set<Role> roles, boolean isBlocked) {
        super(password, firstName, lastName, email, profilePicture);
        this.phone = phone;
        this.roles = roles;
        this.isBlocked = isBlocked;
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
}
