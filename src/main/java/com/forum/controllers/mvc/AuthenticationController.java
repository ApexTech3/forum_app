package com.forum.controllers.mvc;

import com.company.web.springdemo.models.LoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {



    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("Login", new LoginDto());
        return "Login";
    }
}
