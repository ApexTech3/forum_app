package com.forum.controllers.rest;

import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.helpers.AuthenticationHelper;
import com.forum.models.Tag;
import com.forum.models.User;
import com.forum.services.contracts.TagService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagRestController {

    private final TagService tagService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public TagRestController(TagService tagService, AuthenticationHelper authenticationHelper) {
        this.tagService = tagService;
        this.authenticationHelper = authenticationHelper;
    }


    @GetMapping()
    public List<String> getAllTags() {
        return tagService.get().stream().map(Tag::getName).toList();
    }
    @GetMapping("/{tagId}")
    public String getById(@PathVariable int tagId) {
        return tagService.getById(tagId).getName();
    }

    @PostMapping()
    public String createTag(@RequestBody String name) {
        try {
            Tag tag = new Tag();
            tag.setName(name);
            tagService.create(tag);
            return name;
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @SecurityRequirement(name = "Authorization")
    @DeleteMapping("/{tagId}")
    public void deleteTag(@RequestHeader HttpHeaders headers, @PathVariable int tagId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            tagService.delete(tagId, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}

