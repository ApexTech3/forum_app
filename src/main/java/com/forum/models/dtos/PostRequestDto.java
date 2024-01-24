package com.forum.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class PostRequestDto {
    @NotNull(message = "Title can't be empty")
    @Size(min = 2, max = 20, message = "Title should be between 5 and 50 symbols")
    private String title;
    @NotNull(message = "The content can't be empty")
    @Size(min = 2, max = 20, message = "The content should be between 5 and 500 symbols")
    private String content;


    public PostRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}