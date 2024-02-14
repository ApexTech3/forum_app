package com.forum.controllers.mvc;

import com.forum.exceptions.AuthenticationFailureException;
import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.helpers.AuthenticationHelper;
import com.forum.helpers.UserMapper;
import com.forum.models.User;
import com.forum.models.dtos.UserAdminDto;
import com.forum.models.dtos.UserFilterDto;
import com.forum.models.dtos.UserUpdateDto;
import com.forum.models.filters.UserFilterOptions;
import com.forum.services.contracts.PostService;
import com.forum.services.contracts.RoleService;
import com.forum.services.contracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserMvcController {
    private final UserService userService;
    private final RoleService roleService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper mapper;
    private final PostService postService;

    public UserMvcController(UserService userService, RoleService roleService, AuthenticationHelper authenticationHelper, UserMapper mapper, PostService postService) {
        this.userService = userService;
        this.roleService = roleService;
        this.authenticationHelper = authenticationHelper;
        this.mapper = mapper;
        this.postService = postService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession) {
        return httpSession.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping
    public String showAllUsers(@ModelAttribute("filterOptions") UserFilterDto filterDto, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("filterOptions", filterDto);
            model.addAttribute("users", userService.get(
                    new UserFilterOptions(filterDto.getUsername(), filterDto.getEmail(), filterDto.getFirstName(),
                            filterDto.getSortBy(), filterDto.getSortOrder()), user));
            return "usersView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        }
    }

    @GetMapping("/{id}")
    public String showSingleUser(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            tryAuthenticateUser(id, user);
            model.addAttribute("user", userService.get(id));
            return "userView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditUserPage(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            tryAuthenticateUser(id, user);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        }//todo add try catch blocks below
        UserAdminDto dto = mapper.toUpdateAdminDto(userService.get(id));
        model.addAttribute("user", dto);
        model.addAttribute("roles", roleService.get());
        return "userUpdateView";
    }

    @PostMapping("/{id}/edit")
    public String editUser(@PathVariable int id, @ModelAttribute("user") UserAdminDto userDto, BindingResult bindingResult, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            tryAuthenticateUser(id, user);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        }

        if (bindingResult.hasErrors()) {
            return "userUpdateView";
        }

        if (AuthenticationHelper.isAdmin(user)) {
            userService.update(mapper.fromDto(userDto, id), user);
        }
        return "redirect:/users";
    }

    private void tryAuthenticateUser(int id, User user) {
        if (!AuthenticationHelper.isAdmin(user) && user.getId() != id) {
            throw new AuthorizationException("You are not allowed to perform this operation");
        }
    }

    @ModelAttribute("usersCount")
    public long populateUsersCount() {
        return userService.getCount();
    }
    @ModelAttribute("postsCount")
    public long populatePostsCount() {
        return postService.getCount();
    }
}