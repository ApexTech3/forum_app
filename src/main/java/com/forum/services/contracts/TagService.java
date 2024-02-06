package com.forum.services.contracts;

import com.forum.models.Tag;
import com.forum.models.User;

import java.util.List;

public interface TagService {

    List<Tag> get();

    Tag getByName(String name);

    Tag getById(int id);

    Tag create(Tag tag);

    void delete(int id, User user);
}
