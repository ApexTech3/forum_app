package com.forum.controllers;

import com.forum.helpers.AuthenticationHelper;
import com.forum.models.Tag;
import com.forum.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagRestController {

    private final TagService service;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public TagRestController(TagService service, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<Tag> get(){
        return service.get();
    }
}

