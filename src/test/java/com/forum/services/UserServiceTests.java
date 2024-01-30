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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.forum.Helpers.*;

@ExtendWith(MockitoExtension.class)

public class UserServiceTests {
    @Mock
    UserRepository mockRepository;
    @InjectMocks
    UserServiceImpl service;

    @Test
    public void get_Should_ReturnListOfUsers_When_Exists() {
        UserFilterOptions mockFilterOptions = createMockFilterOptions();
        User mockAdmin = createMockAdmin();
        service.get(mockFilterOptions, mockAdmin);

        Mockito.verify(mockRepository, Mockito.times(1)).get(mockFilterOptions);
    }

    @Test
    public void get_Should_Throw_When_UserNotAuthorized() {
        UserFilterOptions mockFilterOptions = createMockFilterOptions();
        User mockUser = createMockUser();

        Assertions.assertThrows(AuthorizationException.class, () -> service.get(mockFilterOptions, mockUser));
    }
}
