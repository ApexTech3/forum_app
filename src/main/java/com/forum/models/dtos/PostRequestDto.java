package com.forum.models.dtos;

import com.forum.models.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;


public class PostRequestDto {
    @NotNull(message = "Title can't be empty")
    @Size(min = 16, max = 64, message = "Title should be between 16 and 64 symbols")
    private String title;
    @NotNull(message = "The content can't be empty")
    @Size(min = 32, max = 8192, message = "The content should be between 32 and 8192 symbols")
    private String content;
    private List<Tag> tags;
    public PostRequestDto() {
    }

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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}