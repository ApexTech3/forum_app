package com.forum.services;

import com.forum.exceptions.AuthorizationException;
import com.forum.models.Comment;
import com.forum.models.User;
import com.forum.repositories.contracts.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.forum.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {
    @Mock
    CommentRepository mockRepository;
    @InjectMocks
    CommentServiceImpl service;

    @Test
    public void getAll_Should_CallRepository_When_MethodCalled() {
        service.get();

        Mockito.verify(mockRepository, Mockito.times(1)).get();
    }

    @Test
    public void getByPostId_Should_CallRepository_When_MethodCalled() {
        service.getByPostId(1);

        Mockito.verify(mockRepository, Mockito.times(1)).getByPostId(1);
    }

    @Test
    public void getByUserId_Should_CallRepository_When_MethodCalled() {
        service.getByUserId(1);

        Mockito.verify(mockRepository, Mockito.times(1)).getByUserId(1);
    }

    @Test
    public void get_Should_CallRepository_When_MethodCalled() {
        service.get(1);

        Mockito.verify(mockRepository, Mockito.times(1)).get(1);
    }

    @Test
    public void create_Should_CallRepository_When_MethodCalled() {
        service.create(createMockComment());

        Mockito.verify(mockRepository, Mockito.times(1)).create(createMockComment());
    }

    @Test
    public void update_Should_Throw_When_UserNotAdminOrRequester() {
        Comment mockComment = createMockComment();
        User mockUser = createMockUser();
        mockUser.setId(2);

        Mockito.when(mockRepository.get(1)).thenReturn(mockComment);

        Assertions.assertThrows(AuthorizationException.class, () -> service.update(mockComment, mockUser));
    }

    @Test
    public void update_Should_CallRepository_When_UserIsAdmin() {
        Comment mockComment = createMockComment();
        User mockAdmin = createMockAdmin();
        mockAdmin.setId(2);
        Mockito.when(mockRepository.get(1)).thenReturn(mockComment);

        service.update(mockComment, mockAdmin);

        Mockito.verify(mockRepository, Mockito.times(1)).update(mockComment);
    }

    @Test
    public void update_Should_CallRepository_When_UserIsRequester() {
        Comment mockComment = createMockComment();
        User mockUser = createMockUser();
        Mockito.when(mockRepository.get(1)).thenReturn(mockComment);

        service.update(mockComment, mockUser);

        Mockito.verify(mockRepository, Mockito.times(1)).update(mockComment);
    }

    @Test
    public void delete_Should_Throw_When_UserNotAdminOrRequester() {
        Comment mockComment = createMockComment();
        User mockUser = createMockUser();
        mockUser.setId(2);

        Mockito.when(mockRepository.get(1)).thenReturn(mockComment);

        Assertions.assertThrows(AuthorizationException.class, () -> service.delete(1, mockUser));
    }

    @Test
    public void delete_Should_CallRepository_When_UserIsAdmin() {
        Comment mockComment = createMockComment();
        User mockAdmin = createMockAdmin();
        mockAdmin.setId(2);
        Mockito.when(mockRepository.get(1)).thenReturn(mockComment);

        service.delete(1, mockAdmin);

        Mockito.verify(mockRepository, Mockito.times(1)).delete(1);
    }

    @Test
    public void delete_Should_CallRepository_When_UserIsRequester() {
        Comment mockComment = createMockComment();
        User mockUser = createMockUser();
        Mockito.when(mockRepository.get(1)).thenReturn(mockComment);

        service.delete(1, mockUser);

        Mockito.verify(mockRepository, Mockito.times(1)).delete(1);
    }
}
