package com.forum.repositories;

import com.forum.models.Tag;

import java.util.List;

public interface TagRepository {

    List<Tag> get();

    Tag getByName(String name);

    Tag getById(int id);

    void create(Tag tag);

    void update(Tag tag);

    void delete(int id);
}
