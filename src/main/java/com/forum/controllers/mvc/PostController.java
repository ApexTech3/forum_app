package com.forum.controllers.mvc;

import com.forum.helpers.AuthenticationHelper;
import com.forum.helpers.CommentMapper;
import com.forum.helpers.PostMapper;
import com.forum.models.Tag;
import com.forum.services.contracts.CommentService;
import com.forum.services.contracts.PostService;
import com.forum.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService service;
    private final CommentService commentService;
    private final TagService tagService;
    private final AuthenticationHelper authenticationHelper;
    private final PostMapper mapper;
    private final CommentMapper commentMapper;

    @Autowired
    public PostController(PostService service, CommentService commentService,
                          AuthenticationHelper authenticationHelper, PostMapper mapper,
                          CommentMapper commentMapper, TagService tagService) {
        this.service = service;
        this.commentService = commentService;
        this.authenticationHelper = authenticationHelper;
        this.mapper = mapper;
        this.commentMapper = commentMapper;
        this.tagService = tagService;
    }

    @ModelAttribute("tags")
    public List<Tag> populateTags() {
        return tagService.get();
    }
}
