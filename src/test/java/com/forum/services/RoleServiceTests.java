package com.forum.services;

import com.forum.repositories.contracts.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTests {
    @Mock
    RoleRepository mockRepository;
    @InjectMocks
    RoleServiceImpl service;

    @Test
    public void get_Should_CallRepository_When_MethodCalled() {
        service.get("USER");

        Mockito.verify(mockRepository, Mockito.times(1)).get("USER");
    }
}
