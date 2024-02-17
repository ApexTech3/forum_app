package com.forum.controllers.mvc;

import com.forum.exceptions.AuthenticationFailureException;
import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityDuplicateException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.helpers.AuthenticationHelper;
import com.forum.helpers.UserMapper;
import com.forum.models.Post;
import com.forum.models.User;
import com.forum.models.dtos.UserDto;
import com.forum.models.dtos.UserFilterDto;
import com.forum.models.dtos.interfaces.AdminUpdate;
import com.forum.models.dtos.interfaces.UserUpdate;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        }
    }

    @GetMapping("/{id}")
    public String showSingleUser(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("user", userService.get(id));
            model.addAttribute("adminOrCurrentUser", user.getId() == id || AuthenticationHelper.isAdmin(userService.get(user.getId())));
            try {
                List<Post> userPosts = postService.getByUserId(id);
                model.addAttribute("userPosts", userPosts);
            } catch (EntityNotFoundException e) {
                model.addAttribute("userPosts", null);
            }
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
        UserDto dto = mapper.toUserDto(userService.get(id));
        model.addAttribute("user", dto);
        model.addAttribute("roles", roleService.get());
        return "userUpdateView";
    }


    @PostMapping("/{id}/edit")
    public String editUser(@PathVariable int id, @Validated(UserUpdate.class) @ModelAttribute("user") UserDto userDto,
                           BindingResult bindingResult, Model model, HttpSession session) {
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
        if (!userDto.getCurrentPassword().equals(user.getPassword())) {
            bindingResult.rejectValue("currentPassword", "error", "Invalid password");
            return "userUpdateView";
        }

        if (!userDto.getPassword().isEmpty() && userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
            userDto.setCurrentPassword(userDto.getPassword());
        } else if (!userDto.getPassword().isEmpty()) {
            bindingResult.rejectValue("password", "error", "Passwords do not match");
            return "userUpdateView";
        }
        try {
            MultipartFile profilePicture = userDto.getProfilePicture();
            if (!profilePicture.isEmpty()) {
                String originalFilename = profilePicture.getOriginalFilename();
                String directory = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
                        + File.separator + "resources" + File.separator + "static" + File.separator + "uploads" + File.separator;
                profilePicture.transferTo(new File(directory + originalFilename));
            }
            userService.update(mapper.fromDto(userDto, id), user);
            session.setAttribute("isAdmin", AuthenticationHelper.isAdmin(userService.get(user.getId())));
            return "redirect:/users/{id}";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("email", "error", e.getMessage());
            return "userUpdateView";
        } catch (IOException e) {
            bindingResult.rejectValue("profilePicture", "auth_error", e.getMessage());
            return "userUpdateView";
        }
    }

    @GetMapping("/{id}/admin-edit")
    public String showAdminEditUserPage(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            if (!AuthenticationHelper.isAdmin(userService.get(user.getId()))) {
                throw new AuthorizationException("You are not allowed to perform this operation");
            }
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        }
        UserDto dto = mapper.toUserDto(userService.get(id));
        model.addAttribute("user", dto);
        model.addAttribute("roles", roleService.get());
        return "UserAdminUpdateView";
    }

    @PostMapping("/{id}/admin-edit")
    public String adminEditUser(@PathVariable int id, @Validated(AdminUpdate.class) @ModelAttribute("user") UserDto userDto,
                                BindingResult bindingResult, Model model, HttpSession session) {
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
            return "UserAdminUpdateView";
        }
        if (!userDto.getPassword().isEmpty() && userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
            userDto.setCurrentPassword(userDto.getPassword());
        } else if (!userDto.getPassword().isEmpty()) {
            bindingResult.rejectValue("password", "error", "Passwords do not match");
            return "UserAdminUpdateView";
        }
        try {
            MultipartFile profilePicture = userDto.getProfilePicture();
            if (!profilePicture.isEmpty()) {
                String originalFilename = profilePicture.getOriginalFilename();
                String directory = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
                        + File.separator + "resources" + File.separator + "static" + File.separator + "uploads" + File.separator;
                profilePicture.transferTo(new File(directory + originalFilename));
            }
            userService.update(mapper.fromDto(userDto, id), user);
            session.setAttribute("isAdmin", AuthenticationHelper.isAdmin(userService.get(user.getId())));
            return "redirect:/users";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("email", "error", e.getMessage());
            return "UserAdminUpdateView";
        }catch (IOException e) {
            bindingResult.rejectValue("profilePicture", "auth_error", e.getMessage());
            return "UserAdminUpdateView";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable int id, Model model, HttpSession session) {
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
        try {
            userService.delete(user, id);
            if (user.getId() == id) {
                session.invalidate();
            }
            return "redirect:/";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
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