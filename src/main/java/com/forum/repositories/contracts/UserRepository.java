package com.forum.repositories.contracts;

import com.forum.models.User;
import com.forum.models.filters.UserFilterOptions;

import java.util.List;

public interface UserRepository {
    User get(int id);

    User get(String username);

    List<User> get(UserFilterOptions filterOptions);

    User register(User user);

    User update(User user);


}
