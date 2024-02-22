package com.forum.controllers.mvc;

import com.forum.services.CloudinaryUploadService;
import com.forum.exceptions.AuthenticationFailureException;
import com.forum.exceptions.EntityDuplicateException;
import com.forum.helpers.AuthenticationHelper;
import com.forum.helpers.UserMapper;
import com.forum.models.User;
import com.forum.models.dtos.UserDto;
import com.forum.models.dtos.interfaces.Login;
import com.forum.models.dtos.interfaces.Register;
import com.forum.services.contracts.PostService;
import com.forum.services.contracts.TagService;
import com.forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/auth")
@SessionAttributes("postFilterOptions")
public class AuthenticationMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final PostService postService;
    private final CloudinaryUploadService cloudinaryUploadService;
    private final TagService tagService;

    public AuthenticationMvcController(AuthenticationHelper authenticationHelper, UserService userService, UserMapper userMapper, PostService postService, CloudinaryUploadService cloudinaryUploadService, TagService tagService) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.postService = postService;
        this.cloudinaryUploadService = cloudinaryUploadService;
        this.tagService = tagService;
    }

    @ModelAttribute
    public void populateAttributes(HttpSession httpSession, Model model) {
        boolean isAuthenticated = httpSession.getAttribute("currentUser") != null;
        model.addAttribute("isAuthenticated", isAuthenticated);

        model.addAttribute("isAdmin", isAuthenticated ? httpSession.getAttribute("isAdmin") : false);
        model.addAttribute("isBlocked", isAuthenticated ? httpSession.getAttribute("isBlocked") : false);

        model.addAttribute("usersCount", userService.getCount());
        model.addAttribute("postsCount", postService.getCount());
        model.addAttribute("tags", tagService.get());

    }

    @ModelAttribute("profilePicturePath")
    public String getProfilePicturePath() {
        return "/uploads/";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new UserDto());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@Validated(Login.class) @ModelAttribute("login") UserDto dto, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            User user = authenticationHelper.verifyAuthentication(dto.getUsername(), dto.getPassword());
            session.setAttribute("currentUser", dto.getUsername());
            session.setAttribute("isAdmin", AuthenticationHelper.isAdmin(user));
            session.setAttribute("isBlocked", AuthenticationHelper.isBlocked(user));
            session.setAttribute("userId", user.getId());
            return "redirect:/";
        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@Validated(Register.class) @ModelAttribute("register") UserDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            if (!dto.getPassword().equals(dto.getPasswordConfirmation())) {
                bindingResult.rejectValue("password", "error", "Passwords do not match");
                return "register";
            }
            User user = userMapper.fromDto(dto);
            MultipartFile profilePicture = dto.getProfilePicture();
            if (!profilePicture.isEmpty()) {
                File pictureFile = File.createTempFile("temp", profilePicture.getOriginalFilename());
                profilePicture.transferTo(pictureFile);
                String pictureUrl = cloudinaryUploadService.uploadImage(pictureFile);
                user.setProfilePicture(pictureUrl);
            }

            userService.register(user);
            return "redirect:/auth/login";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            //email error
            return "register";
        } catch (IOException e) {
            bindingResult.rejectValue("profilePicture", "auth_error", e.getMessage());
            return "register";
        }
    }
}
