package com.forum.models.dtos;

public class UserAdminDto extends UserUpdateDto {
    private String phone;
    private String profilePicture;
    private boolean isAdmin;
    private boolean isBlocked;

    public UserAdminDto() {
    }

    public UserAdminDto(String password, String firstName, String lastName, String email, String phone, String profilePicture, boolean isAdmin, boolean isBlocked) {
        super(password, firstName, lastName, email);
        this.phone = phone;
        this.profilePicture = profilePicture;
        this.isAdmin = isAdmin;
        this.isBlocked = isBlocked;
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
