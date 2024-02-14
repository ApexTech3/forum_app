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

    public User fromRegisterDto(RegisterDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setProfilePicture(userDto.getProfilePicture());
        user.setRoles(Set.of(roleService.get("USER")));
        return user;
    }

    public UserUpdateDto toUpdateDto(User user) {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setUsername(user.getUsername());
        userUpdateDto.setFirstName(user.getFirstName());
        userUpdateDto.setLastName(user.getLastName());
        userUpdateDto.setEmail(user.getEmail());
        userUpdateDto.setProfilePicture(user.getProfilePicture());
        return userUpdateDto;
    }

    public UserAdminDto toUpdateAdminDto(User user) {
        UserAdminDto userAdminDto = new UserAdminDto();
        userAdminDto.setUsername(user.getUsername());
        userAdminDto.setFirstName(user.getFirstName());
        userAdminDto.setLastName(user.getLastName());
        userAdminDto.setEmail(user.getEmail());
        userAdminDto.setPhone(user.getPhone());
        userAdminDto.setRoles(user.getRoles());
        userAdminDto.setProfilePicture(user.getProfilePicture());
        return userAdminDto;
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