package com.forum;

import com.forum.models.*;
import com.forum.models.filters.PostFilterOptions;
import com.forum.models.filters.UserFilterOptions;

import java.util.HashSet;
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
        mockUser.setRoles(new HashSet<>(Set.of(createMockAdminRole(), createMockUserRole())));
        return mockUser;
    }

    public static UserFilterOptions createMockUserFilterOptions() {
        return new UserFilterOptions("username", "fake@email.com", "fakeUser", "mockSort", "mockOrder");
    }

    public static PostFilterOptions createMockPostFilterOptions() {
        return new PostFilterOptions(1, "mockTitle", "mockContext", 1, "mockSort", "mockOrder");
    }

    public static Post createMockPost() {
        Post post = new Post();
        post.setId(1);
        post.setTitle("MockTitle");
        post.setContent("MockContent");
        post.setLikes(1);
        post.setDislikes(1);
        post.setCreatedBy(createMockUser());
        post.setTags(new HashSet<>(Set.of(createMockTag())));
        post.setReplies(new HashSet<>(Set.of(createMockComment())));
        return post;
    }

    public static Comment createMockComment() {
        Comment comment = new Comment();
        comment.setCommentId(1);
        comment.setContent("MockContent");
        comment.setCreatedBy(createMockUser());
        return comment;
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
