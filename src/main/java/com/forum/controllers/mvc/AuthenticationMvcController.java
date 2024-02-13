package com.forum.controllers.mvc;

import com.forum.exceptions.AuthenticationFailureException;
import com.forum.exceptions.EntityDuplicateException;
import com.forum.helpers.AuthenticationHelper;
import com.forum.helpers.UserMapper;
import com.forum.models.User;
import com.forum.models.dtos.LoginDto;
import com.forum.models.dtos.RegisterDto;
import com.forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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


    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto dto, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            User user = authenticationHelper.verifyAuthentication(dto.getUsername(), dto.getPassword());
            session.setAttribute("currentUser", dto.getUsername());
            session.setAttribute("isAdmin", AuthenticationHelper.isAdmin(user));
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
        model.addAttribute("register", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            User user = userService.register(userMapper.fromRegisterDto(dto));
            return "redirect:/";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            //email error
            return "register";
        }
    }
}
