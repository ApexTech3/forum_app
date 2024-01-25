package com.forum.controllers;

import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.helpers.AuthenticationHelper;
import com.forum.helpers.PostMapper;
import com.forum.models.Post;
import com.forum.models.User;
import com.forum.models.dtos.PostRequestDto;
import com.forum.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService service;
    private final AuthenticationHelper authenticationHelper;
    private final PostMapper mapper;
    @Autowired
    public PostRestController(PostService service, AuthenticationHelper authenticationHelper, PostMapper mapper) {
        this.service = service;
        this.authenticationHelper = authenticationHelper;
        this.mapper = mapper;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.get(id);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public List<Post> getPostByUserId(@PathVariable int userId, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.getByUserId(userId);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Post createPost(@RequestHeader HttpHeaders header, @RequestBody PostRequestDto requestDto) {
        try {
            User user = authenticationHelper.tryGetUser(header);
            Post post = mapper.fromRequestDto(requestDto, user);
            service.create(post);
            return post;
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping
    public Post updatePost(@RequestHeader HttpHeaders headers, @RequestBody PostRequestDto requestDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = mapper.fromRequestDto(requestDto, user);
            service.update(post, user);
            return post;
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public void archivePost(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.archive(id, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


}
