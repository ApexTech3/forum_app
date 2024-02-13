package com.forum.services.contracts;

import com.forum.models.Role;

import java.util.List;

public interface RoleService {
    Role get(String role);
    List<Role> get();
}
