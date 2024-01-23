package com.forum.services;

import com.forum.models.User;
import com.forum.models.filters.UserFilterOptions;

public interface UserService {

    User get(String username);

    User get(UserFilterOptions filterOptions, User user);

    User register(User user);

    User update(User user);
}
