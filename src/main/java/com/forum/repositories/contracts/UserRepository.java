package com.forum.repositories.contracts;

import com.forum.models.User;
import com.forum.models.filters.UserFilterOptions;
import org.springframework.data.domain.Page;

public interface UserRepository {
    User get(int id);

    long getCount();

    User get(String username);

    User getByEmail(String email);

    Page<User> get(int page, int size, UserFilterOptions filterOptions);

    User register(User user);

    User update(User user);

    User delete(int id);
}
