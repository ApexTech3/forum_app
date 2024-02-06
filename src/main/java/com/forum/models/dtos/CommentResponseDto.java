package com.forum.models.dtos;

import com.forum.models.Comment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class CommentResponseDto {


    private String content;



    private String creatorName;


    public CommentResponseDto(){}

    public CommentResponseDto(Comment comment) {
        this.content = comment.getContent();
        this.creatorName = comment.getCreatedBy().getUsername();
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}