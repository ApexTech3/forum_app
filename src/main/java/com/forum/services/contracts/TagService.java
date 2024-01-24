package com.forum.services.contracts;

import com.forum.models.Tag;
import com.forum.models.User;

import java.util.List;

public interface TagService {

    List<Tag> get();

    Tag getByName(String name);

    Tag getById(int id);

    void create(Tag tag, User user);

    void delete(int id, User user);
}
