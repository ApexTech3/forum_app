package com.forum.controllers;

import com.forum.helpers.AuthenticationHelper;
import com.forum.models.Comment;
import com.forum.services.contracts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService service;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CommentRestController(CommentService service, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<Comment> get(){
        return service.get();
    }
}
