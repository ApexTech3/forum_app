package com.forum.services;

import com.forum.models.Role;
import com.forum.repositories.contracts.RoleRepository;
import com.forum.services.contracts.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role get(String role) {
        return roleRepository.get(role);
    }

    @Override
    public List<Role> get() {
        return roleRepository.get();
    }
}
