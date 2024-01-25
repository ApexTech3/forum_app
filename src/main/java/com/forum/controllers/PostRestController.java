package com.forum.controllers;

import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.helpers.AuthenticationHelper;
import com.forum.helpers.CommentMapper;
import com.forum.helpers.PostMapper;
import com.forum.models.Comment;
import com.forum.models.Post;
import com.forum.models.User;
import com.forum.models.dtos.CommentRequestDto;
import com.forum.models.dtos.PostRequestDto;
import com.forum.models.dtos.PostResponseDto;
import com.forum.services.contracts.CommentService;
import com.forum.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService service;
    private final CommentService commentService;
    private final AuthenticationHelper authenticationHelper;
    private final PostMapper mapper;
    private final CommentMapper commentMapper;

    @Autowired
    public PostRestController(PostService service, CommentService commentService,AuthenticationHelper authenticationHelper, PostMapper mapper, CommentMapper commentMapper) {
        this.service = service;
        this.commentService = commentService;
        this.authenticationHelper = authenticationHelper;
        this.mapper = mapper;
        this.commentMapper = commentMapper;
    }


    //Post
    @GetMapping
    public List<PostResponseDto> getAllPosts() {
        return service.getAllDto();
    }

    @GetMapping("/byId/{id}")
    public PostResponseDto getPostById(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.getDto(id);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/byUserId/{userId}")
    public List<PostResponseDto> getPostByUserId(@RequestHeader HttpHeaders headers, @PathVariable int userId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.getByUserIdDto(userId);
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

    @PutMapping//todo fix this. Creates new object every time - PostId is needed to be recognized
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



    //Comment
    @GetMapping("/{postId}/comments")
    public List<Comment> getAllPostComments(@PathVariable int postId) {
        return new ArrayList<>(service.get(postId).getReplies());
    }

    @PostMapping("/{postId}/comments")
    public Comment createComment(@RequestHeader HttpHeaders header,@PathVariable int postId, @RequestBody CommentRequestDto requestDto) {
        try {
            User user = authenticationHelper.tryGetUser(header);
            Comment comment = commentMapper.fromRequestDto(requestDto, user, service.get(postId));
            commentService.create(comment);
            return comment;
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("{postId}/comments/{commentId}") //todo is postId needed?
    public Comment editComment(@RequestHeader HttpHeaders headers, @PathVariable int postId ,@PathVariable int commentId, @RequestBody CommentRequestDto requestDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = commentMapper.fromRequestDto(commentId, requestDto, user, service.get(postId));
            comment.setContent(requestDto.getContent());
            commentService.update(comment, user);
            return comment;
        } catch(EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch(AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@RequestHeader HttpHeaders headers, @PathVariable int commentId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            commentService.delete(commentId, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
