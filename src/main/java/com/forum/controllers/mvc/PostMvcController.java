package com.forum.controllers.mvc;

import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.helpers.AuthenticationHelper;
import com.forum.helpers.CommentMapper;
import com.forum.models.Comment;
import com.forum.models.Post;
import com.forum.models.User;
import com.forum.models.dtos.CommentRequestDto;
import com.forum.services.contracts.CommentService;
import com.forum.services.contracts.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/posts")
public class PostMvcController {

    private final PostService postService;

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public PostMvcController(PostService postService, CommentService commentService,
                             CommentMapper commentMapper, AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession) {
        return httpSession.getAttribute("currentUser") != null;
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
            return "ErrorView";
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

}
