package com.forum.repositories.contracts;

import com.forum.models.User;
import com.forum.models.filters.UserFilterOptions;

public interface UserRepository {
    User get(String username);

    User get(UserFilterOptions filterOptions);

    User register(User user);

    User update(User user);


}
