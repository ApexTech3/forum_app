package com.forum.repositories.contracts;

import com.forum.models.Post;
import com.forum.models.Tag;

import java.util.List;

public interface TagRepository {

    List<Tag> get();

    Tag getByName(String name);

    Tag getById(int id);

    Tag create(Tag tag);

    Tag update(Tag tag);

    void delete(int id);
}
