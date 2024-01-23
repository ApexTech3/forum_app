package com.forum.services;

import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityDuplicateException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.models.User;
import com.forum.models.filters.UserFilterOptions;
import com.forum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User get(String username) {
        return repository.get(username);
    }

    @Override
    public User get(UserFilterOptions filterOptions, User user) {
        if (!user.isAdmin())
            throw new AuthorizationException("Only admins can filter search.");
        return repository.get(filterOptions);
    }

    @Override
    public User register(User user) {
        boolean duplicateExists = true;
        try {
            repository.get(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }

        return repository.register(user);
    }

    @Override
    public User update(User user) {
        return repository.update(user);
    }
}
