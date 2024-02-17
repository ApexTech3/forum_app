package com.forum.controllers.rest;

import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityDuplicateException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.helpers.AuthenticationHelper;
import com.forum.helpers.UserMapper;
import com.forum.models.User;
import com.forum.models.dtos.UserDto;
import com.forum.models.dtos.UserAdminDto;
import com.forum.models.dtos.UserResponse;
import com.forum.models.dtos.UserUpdateDto;
import com.forum.models.dtos.interfaces.Register;
import com.forum.models.filters.UserFilterOptions;
import com.forum.services.contracts.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService service;
    private final AuthenticationHelper helper;
    private final UserMapper mapper;

    @Autowired
    public UserRestController(UserService service, AuthenticationHelper helper, UserMapper mapper) {
        this.service = service;
        this.helper = helper;
        this.mapper = mapper;
    }

    @PostMapping
    public UserResponse register(@Validated(Register.class) @RequestBody UserDto registerDto) {
        try {
            User user = mapper.fromDto(registerDto);
            return mapper.toDto(service.register(user));
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping
    public List<UserResponse> get(@RequestHeader HttpHeaders headers, @RequestParam(required = false) String username,
                                  @RequestParam(required = false) String email, @RequestParam(required = false) String firstName,
                                  @RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder) {
        try {
            User user = helper.tryGetUser(headers);
            UserFilterOptions filterOptions = new UserFilterOptions(username, email, firstName, sortBy, sortOrder);
            return service.get(filterOptions, user).stream().map(mapper::toDto).collect(Collectors.toList());
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/count")
    public long get() {
        return service.getCount();
    }

    @SecurityRequirement(name = "Authorization")
    @PutMapping("/{id}")
    public UserResponse updateInfo(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserUpdateDto userUpdateDto,
                                   @PathVariable int id) {
        try {
            User requester = helper.tryGetUser(headers);
            User user = mapper.fromDto(userUpdateDto, id);
            return mapper.toDto(service.update(user, requester));
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @SecurityRequirement(name = "Authorization")
    @PutMapping("/admins/{id}")
    public UserResponse updateAdminInfo(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserAdminDto userAdminDto,
                                        @PathVariable int id) {
        try {
            User requester = helper.tryGetUser(headers);
            User user = mapper.fromDto(userAdminDto, id);
            return mapper.toDto(service.updateAsAdmin(user, requester));
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @SecurityRequirement(name = "Authorization")
    @DeleteMapping("/{id}")
    public User delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = helper.tryGetUser(headers);
            return service.delete(user, id);
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
