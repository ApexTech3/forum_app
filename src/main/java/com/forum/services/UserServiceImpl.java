package com.forum.services;

import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityDuplicateException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.models.User;
import com.forum.models.filters.UserFilterOptions;
import com.forum.repositories.contracts.UserRepository;
import com.forum.services.contracts.RoleService;
import com.forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final String UNAUTHORIZED_USER_ERROR = "You are not authorized to perform this operation";

    private final UserRepository repository;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository repository, RoleService roleService) {
        this.repository = repository;
        this.roleService = roleService;
    }

    @Override
    public User get(int id) {
        return repository.get(id);
    }

    @Override
    public User get(String username) {
        return repository.get(username);
    }

    @Override
    public List<User> get(UserFilterOptions filterOptions, User user) {
        tryAuthorizeAdmin(user);
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
        duplicateExists = true;
        try {
            repository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }
        user.addRole(roleService.get("USER"));
        return repository.register(user);
    }

    @Override
    public User update(User user, User requester) {
        tryAuthorizeUser(requester, user.getUsername());
        checkIfUniqueEmail(user);
        return repository.update(user);
    }

    @Override
    public User updateAsAdmin(User user, User requester) {
        tryAuthorizeAdmin(requester);
        checkIfUniqueEmail(user);
        return repository.update(user);
    }

    @Override
    public User delete(User user, int id) {
        if (!user.isAdmin() && user.getId() != id)
            throw new AuthorizationException(UNAUTHORIZED_USER_ERROR);
        return repository.delete(id);
    }

    private void checkIfUniqueEmail(User user) {
        boolean duplicateExists = true;
        try {
            User userByEmail = repository.getByEmail(user.getEmail());
            if (userByEmail.getId() == user.getId())
                duplicateExists = false;
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }
    }

    private User tryAuthorizeAdmin(User user) {
        if (!user.isAdmin()) {
            throw new AuthorizationException(UNAUTHORIZED_USER_ERROR);
        }
        return user;
    }

    private User tryAuthorizeUser(User user, String username) {
        if (!user.getUsername().equals(username)) {
            throw new AuthorizationException(UNAUTHORIZED_USER_ERROR);
        }
        return user;
    }
}