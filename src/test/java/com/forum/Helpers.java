package com.forum;

import com.forum.models.Role;
import com.forum.models.Tag;
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
        mockUser.setRoles(Set.of(createMockUserRole()));
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
        mockUser.setRoles(Set.of(createMockAdminRole(), createMockUserRole()));
        return mockUser;
    }

    public static UserFilterOptions createMockFilterOptions() {
        return new UserFilterOptions("username", "fake@email.com", "fakeUser", "sort", "order");
    }

    public static Tag createMockTag() {
        Tag tag = new Tag();
        tag.setTagId(1);
        tag.setName("MockTag");
        return tag;
    }

    public static Role createMockAdminRole() {
        return new Role("ADMIN");
    }

    public static Role createMockUserRole() {
        return new Role("USER");
    }
}
