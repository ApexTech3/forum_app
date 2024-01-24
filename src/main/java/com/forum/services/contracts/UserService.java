package com.forum.services.contracts;

import com.forum.models.User;
import com.forum.models.filters.UserFilterOptions;

public interface UserService {

    User get(String username);

    User get(UserFilterOptions filterOptions, User user);

    User register(User user);

    User update(User user);

    User blockUser(User user, String username);

    User unblockUser(User user, String username);
}
