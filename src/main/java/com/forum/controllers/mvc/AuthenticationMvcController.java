package com.forum.controllers.mvc;

import com.forum.exceptions.AuthenticationFailureException;
import com.forum.exceptions.EntityDuplicateException;
import com.forum.helpers.AuthenticationHelper;
import com.forum.helpers.UserMapper;
import com.forum.models.User;
import com.forum.models.dtos.UserDto;
import com.forum.models.dtos.interfaces.Login;
import com.forum.models.dtos.interfaces.Register;
import com.forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final UserMapper userMapper;

    public AuthenticationMvcController(AuthenticationHelper authenticationHelper, UserService userService, UserMapper userMapper) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession) {
        return httpSession.getAttribute("currentUser") != null;
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

    //@RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
    @PostMapping("/register")
    public String handleRegister(@Validated(Register.class) @ModelAttribute("register") UserDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            User user = userMapper.fromDto(dto);
            MultipartFile profilePicture = dto.getProfilePicture();
            if (!profilePicture.isEmpty()) {
                String originalFilename = profilePicture.getOriginalFilename();
                String directory = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
                        + File.separator + "resources" + File.separator + "static" + File.separator + "uploads" + File.separator;
                profilePicture.transferTo(new File(directory + originalFilename));
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
