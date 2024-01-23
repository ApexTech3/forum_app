package com.forum.controllers;

import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityDuplicateException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.helpers.AuthenticationHelper;
import com.forum.helpers.UserMapper;
import com.forum.models.User;
import com.forum.models.dtos.UserDto;
import com.forum.models.filters.UserFilterOptions;
import com.forum.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private static final String UNAUTHORIZED_USER_ERROR = "You are not authorized to browse user information";

    private final UserService service;
    private final AuthenticationHelper helper;
    private final UserMapper mapper;

    @Autowired
    public UserRestController(UserService service, AuthenticationHelper helper, UserMapper mapper) {
        this.service = service;
        this.helper = helper;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody UserDto userDto) {
        try {
            User user = mapper.fromDto(userDto);
            return service.register(user);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping
    public User get(@RequestHeader HttpHeaders headers, @RequestParam(required = false) String username,
                    @RequestParam(required = false) String email, @RequestParam(required = false) String firstName) {
        try {
            User user = helper.tryGetUser(headers);
            UserFilterOptions filterOptions = new UserFilterOptions(username, email, firstName);
            return service.get(filterOptions, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
