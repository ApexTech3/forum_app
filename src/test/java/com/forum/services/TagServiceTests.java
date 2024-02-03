package com.forum.services;

import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityDuplicateException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.models.Tag;
import com.forum.models.User;
import com.forum.repositories.contracts.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.forum.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceTests {
    @Mock
    TagRepository mockRepository;
    @InjectMocks
    TagServiceImpl service;

    @Test
    public void get_Should_CallRepository_When_MethodCalled() {
        service.get();

        Mockito.verify(mockRepository, Mockito.times(1)).get();
    }

    @Test
    public void getByName_Should_CallRepository_When_MethodCalled() {
        service.getByName("MockTag");

        Mockito.verify(mockRepository, Mockito.times(1)).getByName("MockTag");
    }

    @Test
    public void getById_Should_CallRepository_When_MethodCalled() {
        service.getById(1);

        Mockito.verify(mockRepository, Mockito.times(1)).getById(1);
    }

    @Test
    public void create_Should_Throw_When_TagExists() {
        Tag mockTag = createMockTag();
        Mockito.when(mockRepository.getByName(mockTag.getName())).thenReturn(mockTag);

        Assertions.assertThrows(EntityDuplicateException.class, () -> service.create(mockTag));
    }

    @Test
    public void create_Should_CallRepository_When_TagUnique() {
        Tag mockTag = createMockTag();
        Mockito.when(mockRepository.getByName(mockTag.getName())).thenThrow(EntityNotFoundException.class);

        service.create(mockTag);

        Mockito.verify(mockRepository, Mockito.times(1)).create(mockTag);
    }

    @Test
    public void delete_Should_Throw_When_RequesterNotAdmin() {
        User mockUser = createMockUser();

        Assertions.assertThrows(AuthorizationException.class, () -> service.delete(1, mockUser));
    }

    @Test
    public void delete_Should_CallRepository_When_RequesterIsAdmin() {
        User mockAdmin = createMockAdmin();

        service.delete(1, mockAdmin);

        Mockito.verify(mockRepository, Mockito.times(1)).delete(1);

    }
}
