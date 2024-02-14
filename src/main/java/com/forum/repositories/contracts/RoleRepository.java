package com.forum.repositories.contracts;

import com.forum.models.Role;

import java.util.List;

public interface RoleRepository {
    Role get(String role);

    List<Role> get();
}
