package com.forum;

import com.forum.models.Role;
import com.forum.models.User;
import com.forum.models.filters.UserFilterOptions;

import java.util.Set;

public class Helpers {

    public static User createMockUser() {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("MockUsername");
        mockUser.setPassword("MockPassword");
        mockUser.setFirstName("MockFirstName");
        mockUser.setLastName("MockLastName");
        mockUser.setEmail("mock@user.com");
        mockUser.setRoles(Set.of(new Role("USER")));
        return mockUser;
    }

    public static User createMockAdmin() {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("MockUsername");
        mockUser.setPassword("MockPassword");
        mockUser.setFirstName("MockFirstName");
        mockUser.setLastName("MockLastName");
        mockUser.setEmail("mock@user.com");
        mockUser.setRoles(Set.of(new Role("ADMIN"), new Role("USER")));
        return mockUser;
    }

    public static UserFilterOptions createMockFilterOptions() {
        return new UserFilterOptions("username", "fake@email.com", "fakeUser", "sort", "order");
    }
}
