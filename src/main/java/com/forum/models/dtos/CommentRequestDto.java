package com.forum.models.dtos;

import jakarta.validation.constraints.NotBlank;


public class CommentRequestDto {

    @NotBlank(message = "The content can't be empty")
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