package com.forum.helpers;

import com.forum.models.User;
import com.forum.models.dtos.*;
import com.forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final UserService service;

    @Autowired
    public UserMapper(UserService service) {
        this.service = service;
    }

    public User fromDto(UserDto userDto) {
        User user = extractBaseInfo(userDto);
        user.setUsername(userDto.getUsername());
        return user;
    }

    public User fromDto(UserUpdateDto userUpdateDto, String username) {
        User user = extractBaseInfo(userUpdateDto);
        user.setUsername(username);
        user.setId(service.get(user.getUsername()).getId());
        return user;
    }

    public User fromDto(UserAdminDto userAdminDto, String username) {
        User user = extractBaseInfo(userAdminDto);
        user.setPhone(userAdminDto.getPhone());
        user.setProfilePicture(userAdminDto.getProfilePicture());
        user.setBlocked(userAdminDto.isBlocked());
        user.setAdmin(userAdminDto.isAdmin());
        user.setUsername(username);
        user.setId(service.get(user.getUsername()).getId());
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
        return user;
    }

}