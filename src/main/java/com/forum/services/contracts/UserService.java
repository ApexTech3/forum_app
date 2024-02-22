package com.forum.services.contracts;

import com.forum.models.User;
import com.forum.models.filters.UserFilterOptions;
import org.springframework.data.domain.Page;

public interface UserService {

    User get(int id);

    long getCount();

    User get(String username);

    Page<User> get(int page, int size, UserFilterOptions filterOptions, User user);

    User register(User user);

    User update(User user, User requester);

    User updateAsAdmin(User user, User requester);

    User delete(User user, int id);
}
