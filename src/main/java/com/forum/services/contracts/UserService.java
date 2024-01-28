package com.forum.services.contracts;

import com.forum.models.User;
import com.forum.models.filters.UserFilterOptions;

import java.util.List;

public interface UserService {

    User get(int id);

    User get(String username);

    List<User> get(UserFilterOptions filterOptions, User user);

    User register(User user);

    User update(User user, User requester);

    User updateAsAdmin(User user, User requester);
}
