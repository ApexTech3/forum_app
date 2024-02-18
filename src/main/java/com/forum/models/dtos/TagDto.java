package com.forum.models.dtos;

import jakarta.validation.constraints.NotBlank;

public class TagDto {
    @NotBlank(message = "The name can't be empty")
    private String name;

    public TagDto() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
