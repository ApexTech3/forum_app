package com.forum.controllers.mvc;

import com.forum.models.Post;
import com.forum.models.filters.PostFilterOptions;
import com.forum.services.contracts.PostService;
import com.forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    public boolean populateIsAuthenticated(HttpSession httpSession) {
        return httpSession.getAttribute("currentUser") != null;
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
    public String searchPosts(@RequestParam("query") String query, Model model) {
        PostFilterOptions filterOptions = new PostFilterOptions(null, query, query, null, null, null, null);
        List<Post> searchResults = postService.getByContentOrTitle(filterOptions);

        // Add the search results to the model
        model.addAttribute("posts", searchResults);

        // Return the name of the view that should display the search results
        return "mainView";
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
