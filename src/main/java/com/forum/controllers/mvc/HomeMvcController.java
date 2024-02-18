package com.forum.controllers.mvc;

import com.forum.exceptions.EntityNotFoundException;
import com.forum.models.Post;
import com.forum.models.dtos.PostFilterDto;
import com.forum.models.filters.PostFilterOptions;
import com.forum.services.contracts.PostService;
import com.forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
@SessionAttributes("postFilterOptions")
public class HomeMvcController {
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public HomeMvcController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession) {
        return httpSession.getAttribute("currentUser") != null;
    }
    @ModelAttribute("postFilterOptions")
    public PostFilterDto createFilterOptions() {
        return new PostFilterDto();
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
    public String searchPosts(@ModelAttribute("postFilterOptions") PostFilterDto filterDto, Model model) {
        try {
            model.addAttribute("postFilterOptions", filterDto);
            PostFilterOptions filterOptions = new PostFilterOptions(null, filterDto.getQuery(), filterDto.getQuery(),
                    null, null, filterDto.getSortBy(), filterDto.getSortOrder());
            List<Post> searchResults = postService.getByContentOrTitle(filterOptions);
            model.addAttribute("posts", searchResults);
            return "mainView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        }
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
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
