package com.forum.controllers;

import com.forum.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService service;
    @Autowired
    public PostRestController(PostService service) {
        this.service = service;
    }


}