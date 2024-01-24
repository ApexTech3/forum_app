package com.forum.helpers;

import com.forum.models.User;
import com.forum.models.dtos.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User fromDto(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setProfilePicture(userDto.getProfilePicture());
        return user;
    }
}