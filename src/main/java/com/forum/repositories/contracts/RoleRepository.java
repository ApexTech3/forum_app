package com.forum.repositories.contracts;

import com.forum.models.Role;

public interface RoleRepository {
    Role get(String role);
}
