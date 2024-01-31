package com.forum.helpers;

import com.forum.models.Comment;
import com.forum.models.Post;
import com.forum.models.User;
import com.forum.models.dtos.CommentRequestDto;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment fromRequestDto(CommentRequestDto commentRequestDto, User creator, Post parentPost) {
        Comment comment = new Comment();
        comment.setContent(commentRequestDto.getContent());
        comment.setCreatedBy(creator);
        comment.setPostId(parentPost);
        return comment;
    }

    public Comment fromRequestDto(int commentId, CommentRequestDto commentRequestDto, User creator, Post parentPost) {
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setContent(commentRequestDto.getContent());
        comment.setCreatedBy(creator);
        comment.setPostId(parentPost);
        return comment;
    }

    public Comment fromRequestDto(int commentId, CommentRequestDto commentRequestDto) {
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setContent(commentRequestDto.getContent());
        return comment;
    }
}

