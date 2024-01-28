package com.forum.helpers;

import com.forum.models.User;
import com.forum.models.dtos.*;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User fromDto(UserDto userDto) {
        User user = extractBaseInfo(userDto);
        user.setUsername(userDto.getUsername());
        return user;
    }

    public User fromDto(UserUpdateDto userUpdateDto) {
        return extractBaseInfo(userUpdateDto);
    }

    public User fromDto(UserAdminDto userAdminDto) {
        User user = extractBaseInfo(userAdminDto);
        user.setBlocked(userAdminDto.isBlocked());
        user.setAdmin(userAdminDto.isAdmin());
        return user;
    }

    public UserResponse toDto(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setAdmin(user.isAdmin());
        userResponse.setBlocked(user.isBlocked());
        return userResponse;
    }

    private <T extends BaseUserDto> User extractBaseInfo(T dto) {
        User user = new User();
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setProfilePicture(dto.getProfilePicture());
        return user;
    }

}