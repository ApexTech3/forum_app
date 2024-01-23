package com.forum.repositories;

import com.forum.models.User;

public interface UserRepository {
    User get(String username);

    User register(User user);

    User update(User user);


}
