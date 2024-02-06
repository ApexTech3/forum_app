package com.forum.services;

import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityDuplicateException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.models.Post;
import com.forum.models.Tag;
import com.forum.models.User;
import com.forum.models.filters.PostFilterOptions;
import com.forum.repositories.contracts.PostRepository;
import com.forum.repositories.contracts.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.forum.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {
    @Mock
    PostRepository mockRepository;
    @Mock
    TagRepository mockTagRepository;
    @InjectMocks
    PostServiceImpl service;

    @Test
    public void getAll_Should_CallRepository_When_MethodCalled() {
        service.getAll();

        Mockito.verify(mockRepository, Mockito.times(1)).getAll();
    }


    @Test
    public void getCount_Should_CallRepository_When_MethodCalled() {
        service.getCount();

        Mockito.verify(mockRepository, Mockito.times(1)).getCount();
    }

    @Test
    public void getMostCommented_Should_CallRepository_When_MethodCalled() {
        service.getMostCommented();

        Mockito.verify(mockRepository, Mockito.times(1)).getMostCommented();
    }

    @Test
    public void getMostLiked_Should_CallRepository_When_MethodCalled() {
        service.getMostLiked();

        Mockito.verify(mockRepository, Mockito.times(1)).getMostLiked();
    }

    @Test
    public void getRecentlyCreated_Should_CallRepository_When_MethodCalled() {
        service.getRecentlyCreated();

        Mockito.verify(mockRepository, Mockito.times(1)).getRecentlyCreated();
    }

    @Test
    public void get_Should_CallRepository_When_MethodCalled() {
        PostFilterOptions mockPostFilterOptions = createMockPostFilterOptions();
        service.get(mockPostFilterOptions);

        Mockito.verify(mockRepository, Mockito.times(1)).get(mockPostFilterOptions);
    }

    @Test
    public void getById_Should_CallRepository_When_MethodCalled() {
        service.getById(1);

        Mockito.verify(mockRepository, Mockito.times(1)).get(1);
    }

    @Test
    public void getByTitle_Should_CallRepository_When_MethodCalled() {
        service.getByTitle("title");

        Mockito.verify(mockRepository, Mockito.times(1)).getByTitle("title");
    }

    @Test
    public void getByContent_Should_CallRepository_When_MethodCalled() {
        service.getByContent("title");

        Mockito.verify(mockRepository, Mockito.times(1)).getByContent("title");
    }

    @Test
    public void getByUserId_Should_CallRepository_When_MethodCalled() {
        service.getByUserId(1);

        Mockito.verify(mockRepository, Mockito.times(1)).getByUserId(1);
    }

    @Test
    public void create_Should_Throw_When_TitleNotUnique() {
        Post mockPost = createMockPost();

        Mockito.when(mockRepository.getByTitle(mockPost.getTitle())).thenReturn(List.of(mockPost));

        Assertions.assertThrows(EntityDuplicateException.class, () -> service.create(mockPost));
    }

    @Test
    public void create_Should_CallRepository_When_TitleUnique() {
        Post mockPost = createMockPost();

        Mockito.when(mockRepository.getByTitle(mockPost.getTitle())).thenThrow(EntityNotFoundException.class);

        service.create(mockPost);

        Mockito.verify(mockRepository, Mockito.times(1)).create(mockPost);
    }

    @Test
    public void update_Should_Throw_When_UserNotAdminOrRequester() {
        Post mockPost = createMockPost();
        User mockUser = createMockUser();
        User mockUser2 = createMockUser();
        mockUser2.setId(2);
        mockPost.setCreatedBy(mockUser2);

        Assertions.assertThrows(AuthorizationException.class, () -> service.update(mockPost, mockUser));
    }

    @Test
    public void update_Should_CallRepository_When_UserIsRequester() {
        Post mockPost = createMockPost();
        User mockUser = createMockUser();

        service.update(mockPost, mockUser);

        Mockito.verify(mockRepository, Mockito.times(1)).update(mockPost);
    }

    @Test
    public void update_Should_CallRepository_When_UserIsAdmin() {
        Post mockPost = createMockPost();
        User mockAdmin = createMockAdmin();
        mockAdmin.setId(2);

        service.update(mockPost, mockAdmin);

        Mockito.verify(mockRepository, Mockito.times(1)).update(mockPost);
    }

    @Test
    public void archive_Should_Throw_When_UserNotAdminOrRequester() {
        Post mockPost = createMockPost();
        User mockUser = createMockUser();
        mockUser.setId(2);
        Mockito.when(mockRepository.get(1)).thenReturn(mockPost);

        Assertions.assertThrows(AuthorizationException.class, () -> service.archive(1, mockUser));
    }

    @Test
    public void archive_Should_CallRepository_When_UserIsRequester() {
        Post mockPost = createMockPost();
        User mockUser = createMockUser();
        Mockito.when(mockRepository.get(1)).thenReturn(mockPost);

        service.archive(1, mockUser);

        Mockito.verify(mockRepository, Mockito.times(1)).archive(1);

    }

    @Test
    public void archive_Should_CallRepository_When_UserIsAdmin() {
        User mockAdmin = createMockAdmin();

        service.archive(1, mockAdmin);

        Mockito.verify(mockRepository, Mockito.times(1)).archive(1);
    }

    @Test
    public void like_Should_CallRepository_When_MethodCalled() {
        service.like(createMockUser(), 1);

        Mockito.verify(mockRepository, Mockito.times(1)).likeDislike(1, 1, "LIKE");
    }

    @Test
    public void dislike_Should_CallRepository_When_MethodCalled() {
        service.dislike(createMockUser(), 1);

        Mockito.verify(mockRepository, Mockito.times(1)).likeDislike(1, 1, "DISLIKE");
    }

    @Test
    public void associateTagWithPost_Should_Throw_When_PostNotExists() {
//todo
    }

    @Test
    public void associateTagWithPost_Should_Throw_When_TagNotExists() {
//todo
    }

    @Test
    public void dissociateTagWithPost_Should_Throw_When_TagNotInPost() {
        Tag mockTag = new Tag();
        mockTag.setTagId(2);
        mockTag.setName("mockTag2");
        Mockito.when(mockRepository.get(1)).thenReturn(createMockPost());
        Mockito.when(mockTagRepository.getById(2)).thenReturn(mockTag);
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.dissociateTagWithPost(1, 2));

    }
}
