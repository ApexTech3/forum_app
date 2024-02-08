package com.forum.controllers.mvc;

import com.forum.services.contracts.PostService;
import com.forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public HomeMvcController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession){
        return httpSession.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showHomePage(Model model) {
        model.addAttribute("usersCount", userService.getCount());
        model.addAttribute("postsCount", postService.getCount());
        model.addAttribute("post1", postService.getAll().get(0));
        return "mainView";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }
}
