package com.forum.controllers.mvc;


import com.forum.exceptions.EntityNotFoundException;
import com.forum.models.Post;
import com.forum.models.dtos.PostFilterDto;
import com.forum.models.filters.PostFilterOptions;

import com.forum.services.contracts.PostService;
import com.forum.services.contracts.TagService;
import com.forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/")
@SessionAttributes("postFilterOptions")
public class HomeMvcController {
    private final UserService userService;
    private final PostService postService;
    private final TagService tagService;

    @Autowired
    public HomeMvcController(UserService userService, PostService postService, TagService tagService) {
        this.userService = userService;
        this.postService = postService;
        this.tagService = tagService;
    }

    @ModelAttribute
    public void populateAttributes(HttpSession httpSession, Model model) {
        boolean isAuthenticated = httpSession.getAttribute("currentUser") != null;
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("postFilterOptions", new PostFilterDto());

        model.addAttribute("isAdmin", isAuthenticated ? httpSession.getAttribute("isAdmin") : false);
        model.addAttribute("isBlocked", isAuthenticated ? httpSession.getAttribute("isBlocked") : false);

        model.addAttribute("usersCount", userService.getCount());
        model.addAttribute("postsCount", postService.getCount());
        model.addAttribute("tags", tagService.get());

    }


    @GetMapping
    public String getAllPosts(@RequestParam(name = "page", defaultValue = "1") int page,
                              @RequestParam(name = "size", defaultValue = "10") int size,
                              Model model) {
        Page<Post> postPage = postService.getAllPosts(page, size);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", postPage.getNumber() + 1);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("showPagination", true);

        return "mainView";
    }


    @GetMapping("/search")
    public String searchPosts(@ModelAttribute("postFilterOptions") PostFilterDto filterDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "userUpdateView";
        }
        try {
            PostFilterOptions filterOptions = new PostFilterOptions(null, filterDto.getQuery(), filterDto.getQuery(),
                    null, null, filterDto.getSortBy(), filterDto.getSortOrder());
            List<Post> searchResults = postService.getByContentOrTitle(filterOptions);
            model.addAttribute("posts", searchResults);
            return "mainView";
        } catch (EntityNotFoundException e) {
            bindingResult.rejectValue("query", "error", e.getMessage());
            return "mainView";
        }
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }
}

