package com.forum.controllers.rest;

import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityDuplicateException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.helpers.AuthenticationHelper;
import com.forum.helpers.CommentMapper;
import com.forum.helpers.PostMapper;
import com.forum.models.Comment;
import com.forum.models.Post;
import com.forum.models.User;
import com.forum.models.dtos.CommentRequestDto;
import com.forum.models.dtos.CommentResponseDto;
import com.forum.models.dtos.PostRequestDto;
import com.forum.models.dtos.PostResponseDto;
import com.forum.models.filters.PostFilterOptions;
import com.forum.services.contracts.CommentService;
import com.forum.services.contracts.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
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
    public PostRestController(PostService service, CommentService commentService, AuthenticationHelper authenticationHelper, PostMapper mapper, CommentMapper commentMapper) {
        this.service = service;
        this.commentService = commentService;
        this.authenticationHelper = authenticationHelper;
        this.mapper = mapper;
        this.commentMapper = commentMapper;
    }

    //no registration
    @GetMapping("/count")
    public long get() {
        return service.getCount();
    }

    @GetMapping("/mostCommented")
    public List<PostResponseDto> getMostCommented() {
        return mapper.fromPostListToResponseDto(service.getMostCommented());
    }

    @GetMapping("/mostLiked")
    public List<PostResponseDto> getMostLiked() {
        return mapper.fromPostListToResponseDto(service.getMostLiked());
    }

    @GetMapping("/new")
    public List<PostResponseDto> getRecentlyCreated() {
        return mapper.fromPostListToResponseDto(service.getRecentlyCreated());
    }


    @GetMapping
    public List<PostResponseDto> get(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Integer creatorId,
            @RequestParam(required = false) List<Integer> tags,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder
    ) {
        try {
            PostFilterOptions filterOptions = new PostFilterOptions(id, title, content, creatorId, tags ,sortBy, sortOrder);
            return mapper.fromPostListToResponseDto(service.get(filterOptions));
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @SecurityRequirement(name = "Authorization")
    @GetMapping("/byId/{id}")
    public PostResponseDto getPostById(@PathVariable int id,
                                       @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            List<Post> postListOfOne = new ArrayList<>();
            postListOfOne.add(service.getById(id));
            return mapper.fromPostListToResponseDto(postListOfOne).get(0);
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/byUserId/{userId}")
    public List<PostResponseDto> getPostByUserId(@RequestHeader HttpHeaders headers,
                                                 @PathVariable int userId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return mapper.fromPostListToResponseDto(service.getByUserId(userId));
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/byTitle/{sentence}")
    public List<PostResponseDto> getByWordInTitle(@RequestHeader HttpHeaders headers,
                                                  @PathVariable String sentence) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return mapper.fromPostListToResponseDto(service.getBySimilarTitle(sentence));
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping("/byContent/{sentence}")
    public List<PostResponseDto> getByWordInTitle(@PathVariable String sentence,
                                                  @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return mapper.fromPostListToResponseDto(service.getByContent(sentence));
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping
    public Post createPost(@RequestHeader HttpHeaders header,
                           @Valid @RequestBody PostRequestDto requestDto) {
        try {
            User user = authenticationHelper.tryGetUser(header);
            Post post = mapper.fromRequestDto(requestDto, user);
            service.create(post);
            return post;
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @SecurityRequirement(name = "Authorization")
    @PutMapping("/update/{id}")
    public Post updatePost(@RequestHeader HttpHeaders headers,
                           @PathVariable int id, @Valid
                           @RequestBody PostRequestDto requestDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = mapper.fromRequestDto(id, requestDto, user);
            service.update(post, user);
            return post;
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @SecurityRequirement(name = "Authorization")
    @PutMapping("/{id}")
    public void archivePost(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.archive(id, user);
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    //Comment
    @GetMapping("/{postId}/comments")
    public List<CommentResponseDto> getAllPostComments(@PathVariable int postId) {
        return service.getById(postId).getReplies().stream()
                .map(CommentResponseDto::new).toList();
    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping("/{postId}/comments")
    public CommentResponseDto createComment(@RequestHeader HttpHeaders header,
                                            @PathVariable int postId,
                                            @RequestBody CommentRequestDto requestDto) {
        try {
            User user = authenticationHelper.tryGetUser(header);
            Comment comment = commentMapper.fromRequestDto(requestDto, user, service.getById(postId));
            commentService.create(comment);
            return new CommentResponseDto(comment);
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @SecurityRequirement(name = "Authorization")
    @PutMapping("/comments/{commentId}")
    public CommentResponseDto editComment(@RequestHeader HttpHeaders headers,
                                          @PathVariable int commentId,
                                          @RequestBody CommentRequestDto requestDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = commentService.get(commentId);
            comment.setContent(requestDto.getContent());
            commentService.update(comment, user);
            return new CommentResponseDto(comment);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @SecurityRequirement(name = "Authorization")
    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@RequestHeader HttpHeaders headers, @PathVariable int commentId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            commentService.delete(commentId, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    //Like/Dislike

    @SecurityRequirement(name = "Authorization")
    @PostMapping("/like/{postId}")
    public PostResponseDto likePost(@RequestHeader HttpHeaders headers, @PathVariable int postId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return mapper.toPostResponseDto(service.like(user, postId));
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping("/dislike/{postId}")
    public PostResponseDto dislikePost(@RequestHeader HttpHeaders headers, @PathVariable int postId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return mapper.toPostResponseDto(service.dislike(user, postId));
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    //TagHandling

    @SecurityRequirement(name = "Authorization")
    @PostMapping("/{postId}/tags/{tagId}")
    public HttpStatus addTagToPost(@RequestHeader HttpHeaders headers, @PathVariable int postId, @PathVariable int tagId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.associateTagWithPost(postId, tagId);
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return HttpStatus.OK;
    }

    @SecurityRequirement(name = "Authorization")
    @DeleteMapping("/{postId}/tags/{tagId}")
    public HttpStatus removeTagFromPost(@RequestHeader HttpHeaders headers, @PathVariable int postId, @PathVariable int tagId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.dissociateTagWithPost(postId, tagId);
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return HttpStatus.OK;
    }

}
