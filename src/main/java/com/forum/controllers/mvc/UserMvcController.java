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
import com.forum.services.contracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserMvcController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper mapper;
    private final PostService postService;

    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper mapper, PostService postService) {
        this.userService = userService;
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
            model.addAttribute("user", user);
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
        }
        if (AuthenticationHelper.isAdmin(user)) {
            UserAdminDto dto = mapper.toUpdateAdminDto(userService.get(id));
            model.addAttribute("user", dto);
            return "userAdminView";
        } else {
            UserUpdateDto dto = mapper.toUpdateDto(user);
            model.addAttribute("user", dto);
            return "userView";
        }
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