package com.forum.services;

import com.forum.exceptions.AuthorizationException;
import com.forum.models.User;
import com.forum.models.filters.UserFilterOptions;
import com.forum.repositories.contracts.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.forum.Helpers.createMockFilterOptions;
import static com.forum.Helpers.createMockUser;

@ExtendWith(MockitoExtension.class)

public class UserServiceTests {
    @Mock
    UserRepository mockRepository;
    @InjectMocks
    UserServiceImpl service;

    @Test
    public void get_Should_Throw_When_UserNotAuthorized() {
        UserFilterOptions mockFilterOptions = createMockFilterOptions();
        User mockUser = createMockUser();

        Assertions.assertThrows(AuthorizationException.class, () -> service.get(mockFilterOptions, mockUser));
    }
}
