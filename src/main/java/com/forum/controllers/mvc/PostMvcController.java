package com.forum.controllers.mvc;

import com.forum.exceptions.AuthenticationFailureException;
import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.helpers.AuthenticationHelper;
import com.forum.helpers.CommentMapper;
import com.forum.helpers.PostMapper;
import com.forum.models.Comment;
import com.forum.models.Post;
import com.forum.models.Tag;
import com.forum.models.User;
import com.forum.models.dtos.CommentRequestDto;
import com.forum.models.dtos.PostRequestDto;
import com.forum.models.dtos.TagDto;
import com.forum.models.filters.PostFilterOptions;
import com.forum.services.contracts.CommentService;
import com.forum.services.contracts.PostService;
import com.forum.services.contracts.TagService;
import com.forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostMvcController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final AuthenticationHelper authenticationHelper;
    private final TagService tagService;
    private final UserService userService;


    @Autowired
    public PostMvcController(PostService postService, PostMapper postMapper ,
                             CommentService commentService, CommentMapper commentMapper,
                             AuthenticationHelper authenticationHelper, TagService tagService, UserService userService) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.authenticationHelper = authenticationHelper;
        this.tagService = tagService;
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession) {
        return httpSession.getAttribute("currentUser") != null;
    }

    @ModelAttribute("tags")
    public List<Tag> populateTags() {
        return tagService.get();
    }

    @GetMapping("/search")
    public String searchPosts(@RequestParam("searchQuery") String searchQuery, Model model) {
        PostFilterOptions filterOptions = new PostFilterOptions(null, null, searchQuery, null, new ArrayList<>(), null, null);

        List<Post> searchResults = postService.get(filterOptions);

        model.addAttribute("posts", searchResults);

        return "mainView";
    }

    @GetMapping("/mostLiked")
    public String showMostLikedPosts( Model model) {
        List<Post> mostLikedPosts = postService.getMostLiked();

        model.addAttribute("posts", mostLikedPosts);
        model.addAttribute("showPagination", false);

        return "mainView";
    }

    @GetMapping("/mostCommented")
    public String showMostCommentedPosts(Model model) {
        List<Post> mostCommentedPosts = postService.getMostCommented();

        model.addAttribute("posts", mostCommentedPosts);
        model.addAttribute("showPagination", false);

        return "mainView";
    }

    @GetMapping("/{id}")
    public String showSinglePost(@PathVariable int id, Model model) {
        try {
            Post post = postService.getById(id);
            model.addAttribute("post", post);
            model.addAttribute("newComment", new CommentRequestDto());
            return "postView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "errorView";
        }
    }


    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable int id,@NotNull @ModelAttribute("newComment") CommentRequestDto comment, HttpSession httpSession) {
        try {
            User user = authenticationHelper.tryGetUser(httpSession);
            Comment newComment = commentMapper.fromRequestDto(comment, user, postService.getById(id));
            commentService.create(newComment);
            return "redirect:/posts/" + id;
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}/like")
    public String likePost(@PathVariable int id, HttpSession httpSession) {
        User user = authenticationHelper.tryGetUser(httpSession);
        postService.like(user, id);
        return "redirect:/posts/" + id;
    }

    @GetMapping("/{id}/dislike")
    public String dislikePost(@PathVariable int id, HttpSession httpSession) {
        User user = authenticationHelper.tryGetUser(httpSession);
        postService.dislike(user, id);
        return "redirect:/posts/" + id;
    }

    @GetMapping("/new")
    public String showNewPostPage(Model model, HttpSession httpSession) {
        try{
            authenticationHelper.tryGetUser(httpSession);
        } catch(AuthenticationFailureException e){
            return "redirect:/auth/login";
        }
        model.addAttribute("postDto", new PostRequestDto());
        return "newPostView";
    }

    @PostMapping("/new")
    public String createPost(@Valid @ModelAttribute("postDto") PostRequestDto postRequestDto,
                             BindingResult bindingResult,
                             @RequestParam("tagsList") List<String> tagsList,
                             Model model,
                             HttpSession httpSession) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(httpSession);
        } catch(AuthenticationFailureException e) {
            // Log the exception
            System.out.println("Authentication failed: " + e.getMessage());
            return "redirect:/auth/login";
        }

        if(bindingResult.hasErrors()) {
            // Log the validation errors
            System.out.println("Validation errors: " + bindingResult.getAllErrors());
            return "newPostView";
        }
        try {
            if(tagsList != null) {
                List<Tag> tags = tagsList.stream().map(tagService::getByName).toList();
                postRequestDto.setTags(tags);
            }
            Post post = postMapper.fromRequestDtoWithTags(postRequestDto, user);
            postService.create(post);
        } catch(Exception e) {
            // Log any other exceptions
            System.out.println("Error creating post: " + e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/newTag")
    public String createTag(@Valid @ModelAttribute("tagDto") TagDto tagDto, BindingResult bindingResult, Model model,
                            HttpSession httpSession) {
        if(bindingResult.hasErrors()) {
            // Log the validation errors
            System.out.println("Validation errors: " + bindingResult.getAllErrors());
            return "newPostView";
        }
        try {
            Tag tag = postMapper.fromTagDto(tagDto.getName());
            tagService.create(tag);
        } catch(Exception e) {
            // Log any other exceptions
            System.out.println("Error creating tag: " + e.getMessage());
        }
        return "redirect:/posts/new";
    }

    @ModelAttribute("tagDto")
    public TagDto tagDto() {
        return new TagDto();
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
