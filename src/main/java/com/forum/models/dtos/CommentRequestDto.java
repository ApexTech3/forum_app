package com.forum.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class CommentRequestDto {

    @NotNull(message = "The content can't be empty")
    @Size(min = 2, max = 300, message = "The comment should be between 2 and 300 symbols")
    private String content;


    public CommentRequestDto(){}

    public CommentRequestDto(String content) {
        this.content = content;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}