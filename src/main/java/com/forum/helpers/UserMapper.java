package com.forum.helpers;

import com.forum.models.User;
import com.forum.models.dtos.*;
import com.forum.services.contracts.RoleService;
import com.forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final UserService service;
    private final RoleService roleService;

    @Autowired
    public UserMapper(UserService service, RoleService roleService) {
        this.service = service;
        this.roleService = roleService;
    }

    public User fromDto(UserUpdateDto userUpdateDto, int id) {
        return extractBaseInfo(userUpdateDto, id);
    }

    public User fromDto(UserAdminDto userAdminDto, int id) {
        User user = service.get(id);
        user.setPassword(userAdminDto.getPassword().isEmpty() ? user.getPassword() : userAdminDto.getPassword());
        user.setFirstName(userAdminDto.getFirstName());
        user.setLastName(userAdminDto.getLastName());
        user.setEmail(userAdminDto.getEmail());
        user.setProfilePicture(userAdminDto.getProfilePicture());
        user.setPhone(userAdminDto.getPhone());
        userAdminDto.getRoles().add(roleService.get("USER"));
        user.setRoles(userAdminDto.getRoles().stream().map(r -> roleService.get(r.getRole())).collect(Collectors.toSet()));
        user.setBlocked(userAdminDto.isBlocked());
        return user;
    }

    public User fromDto(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        if (!userDto.getProfilePicture().isEmpty()) {
            user.setProfilePicture("/uploads/" + userDto.getProfilePicture().getOriginalFilename());
        }
        if (userDto.getRoles() == null) {
            userDto.setRoles(Set.of(roleService.get("USER")));
        } else {
            userDto.getRoles().add(roleService.get("USER"));
        }
        user.setRoles(userDto.getRoles().stream().map(r -> roleService.get(r.getRole())).collect(Collectors.toSet()));
        user.setBlocked(userDto.isBlocked());
        return user;
    }

    public User fromDto(UserDto userDto, int id) {
        User user = service.get(id);
        if (!userDto.getPassword().isEmpty() && !userDto.getPasswordConfirmation().isEmpty()
                && userDto.getPassword().equals(userDto.getPasswordConfirmation()))
            user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        if (!userDto.getProfilePicture().isEmpty()) {
            user.setProfilePicture("/uploads/" + userDto.getProfilePicture().getOriginalFilename());
        }
        if (userDto.getRoles() == null) {
            userDto.setRoles(Set.of(roleService.get("USER")));
        } else {
            userDto.getRoles().add(roleService.get("USER"));
        }
        user.setRoles(userDto.getRoles().stream().map(r -> roleService.get(r.getRole())).collect(Collectors.toSet()));
        user.setBlocked(userDto.isBlocked());
        user.setDeleted(userDto.isDeleted());
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

    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setRoles(user.getRoles());
        userDto.setBlocked(user.isBlocked());
        userDto.setDeleted(user.isDeleted());
        return userDto;
    }

    private <T extends BaseUserDto> User extractBaseInfo(T dto, int id) {
        User user = service.get(id);
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setProfilePicture(dto.getProfilePicture());
        return user;
    }

}