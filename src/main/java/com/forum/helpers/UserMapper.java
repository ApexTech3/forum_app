package com.forum.helpers;

import com.forum.models.User;
import com.forum.models.dtos.*;
import com.forum.services.contracts.RoleService;
import com.forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final UserService service;
    private final RoleService roleService;

    @Autowired
    public UserMapper(UserService service, RoleService roleService) {
        this.service = service;
        this.roleService = roleService;
    }

    public User fromDto(UserDto userDto) {
        User user = extractBaseInfo(userDto);
        user.setUsername(userDto.getUsername());
        return user;
    }

    public User fromDto(UserUpdateDto userUpdateDto, int id) {
        User user = extractBaseInfo(userUpdateDto);
        user.setId(id);
        user.setUsername(service.get(id).getUsername());
        user.setRoles(service.get(id).getRoles());
        return user;
    }

    public User fromDto(UserAdminDto userAdminDto, int id) {
        User user = extractBaseInfo(userAdminDto);
        user.setPhone(userAdminDto.getPhone());
        user.setProfilePicture(userAdminDto.getProfilePicture());
        user.setBlocked(userAdminDto.isBlocked());
        user.setId(id);
        user.setUsername(service.get(id).getUsername());
        user.setRoles(service.get(id).getRoles());
        return user;
    }

    public UserResponse toDto(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        user.setProfilePicture(user.getProfilePicture());
        userResponse.setRoles(user.getRoles());
        userResponse.setBlocked(user.isBlocked());
        return userResponse;
    }

    private <T extends BaseUserDto> User extractBaseInfo(T dto) {
        User user = new User();
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setProfilePicture(dto.getProfilePicture());
        return user;
    }

}