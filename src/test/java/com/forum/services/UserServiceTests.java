package com.forum.services;

import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityDuplicateException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.models.User;
import com.forum.models.filters.UserFilterOptions;
import com.forum.repositories.contracts.UserRepository;
import com.forum.services.contracts.RoleService;
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
    @Mock
    RoleService mockRoleService;
    @InjectMocks
    UserServiceImpl service;

    @Test
    public void getById_Should_ReturnUser_When_Exists() {
        User mockUser = createMockUser();
        Mockito.when(mockRepository.get(1)).thenReturn(mockUser);

        User result = service.get(1);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(mockUser.getUsername(), result.getUsername());
        Assertions.assertEquals(mockUser.getPassword(), result.getPassword());
        Assertions.assertEquals(mockUser.getFirstName(), result.getFirstName());
        Assertions.assertEquals(mockUser.getLastName(), result.getLastName());
        Assertions.assertEquals(mockUser.getEmail(), result.getEmail());
        Assertions.assertEquals(mockUser.getRoles(), result.getRoles());
    }

    @Test
    public void getCount_Should_ReturnCount_When_Exists() {
        Mockito.when(mockRepository.getCount()).thenReturn(1L);

        Assertions.assertEquals(1, service.getCount());
    }

    @Test
    public void getByName_Should_ReturnUser_When_MatchExists() {
        User mockUser = createMockUser();
        Mockito.when(mockRepository.get("MockUsername")).thenReturn(mockUser);

        User result = service.get("MockUsername");

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(mockUser.getUsername(), result.getUsername());
        Assertions.assertEquals(mockUser.getPassword(), result.getPassword());
        Assertions.assertEquals(mockUser.getFirstName(), result.getFirstName());
        Assertions.assertEquals(mockUser.getLastName(), result.getLastName());
        Assertions.assertEquals(mockUser.getEmail(), result.getEmail());
        Assertions.assertEquals(mockUser.getRoles(), result.getRoles());
    }

    @Test
    public void get_Should_ReturnListOfUsers_When_Exists() {
        UserFilterOptions mockFilterOptions = createMockUserFilterOptions();
        User mockAdmin = createMockAdmin();
        service.get(mockFilterOptions, mockAdmin);

        Mockito.verify(mockRepository, Mockito.times(1)).get(mockFilterOptions);
    }

    @Test
    public void get_Should_Throw_When_UserNotAuthorized() {
        UserFilterOptions mockFilterOptions = createMockUserFilterOptions();
        User mockUser = createMockUser();

        Assertions.assertThrows(AuthorizationException.class, () -> service.get(mockFilterOptions, mockUser));
    }

    @Test
    public void register_Should_Throw_When_UsernameIsNotUnique() {
        User mockUser = createMockUser();
        Mockito.when(mockRepository.get(mockUser.getUsername())).thenReturn(mockUser);
        Assertions.assertThrows(EntityDuplicateException.class, () -> service.register(createMockUser()));
    }

    @Test
    public void register_Should_Throw_When_EmailIsNotUnique() {
        User mockUser = createMockUser();
        Mockito.when(mockRepository.get(mockUser.getUsername())).thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByEmail(mockUser.getEmail())).thenReturn(mockUser);

        Assertions.assertThrows(EntityDuplicateException.class, () -> service.register(mockUser));
    }

    @Test
    public void register_Should_CallRepository_When_UsernameAndEmailAreUnique() {
        User mockUser = createMockUser();
        Mockito.when(mockRepository.get(mockUser.getUsername())).thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByEmail(mockUser.getEmail())).thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRoleService.get("USER")).thenReturn(createMockUserRole());

        service.register(mockUser);

        Mockito.verify(mockRepository, Mockito.times(1)).register(mockUser);
    }

    @Test
    public void update_Should_Throw_When_UserIsNotTheRequester() {
        User mockUser = createMockUser();
        User mockUser2 = createMockUser();
        mockUser2.setUsername("DifferentMockUsername");

        Assertions.assertThrows(AuthorizationException.class, () -> service.update(mockUser, mockUser2));
    }

    @Test
    public void update_Should_Throw_When_EmailIsNotUnique() {
        User mockUser = createMockUser();
        User mockUser2 = createMockUser();
        mockUser2.setId(2);
        Mockito.when(mockRepository.getByEmail(mockUser.getEmail())).thenReturn(mockUser2);

        Assertions.assertThrows(EntityDuplicateException.class, () -> service.update(mockUser, mockUser));
    }

    @Test
    public void update_Should_CallRepository_When_UserIsAuthorizedAndEmailIsSame() {
        User mockUser = createMockUser();
        Mockito.when(mockRepository.getByEmail(mockUser.getEmail())).thenReturn(mockUser);

        service.update(mockUser, mockUser);

        Mockito.verify(mockRepository, Mockito.times(1)).update(mockUser);
    }

    @Test
    public void update_Should_CallRepository_When_UserIsAuthorizedAndNewEmailIsUnique() {
        User mockUser = createMockUser();
        Mockito.when(mockRepository.getByEmail(mockUser.getEmail())).thenThrow(EntityNotFoundException.class);

        service.update(mockUser, mockUser);

        Mockito.verify(mockRepository, Mockito.times(1)).update(mockUser);
    }

    @Test
    public void updateAsAdmin_Should_Throw_When_UserIsNotAdmin() {
        User mockUser = createMockUser();

        Assertions.assertThrows(AuthorizationException.class, () -> service.updateAsAdmin(mockUser, mockUser));
    }

    @Test
    public void updateAsAdmin_Should_Throw_When_EmailIsNotUnique() {
        User mockUser = createMockUser();
        User mockAdmin = createMockAdmin();
        mockAdmin.setId(2);
        Mockito.when(mockRepository.getByEmail(mockUser.getEmail())).thenReturn(mockAdmin);

        Assertions.assertThrows(EntityDuplicateException.class, () -> service.updateAsAdmin(mockUser, mockAdmin));

    }

    @Test
    public void updateAsAdmin_Should_CallRepository_When_UserIsAuthorizedAndEmailIsSame() {
        User mockUser = createMockUser();
        User mockAdmin = createMockAdmin();
        Mockito.when(mockRepository.getByEmail(mockUser.getEmail())).thenReturn(mockUser);

        service.updateAsAdmin(mockUser, mockAdmin);

        Mockito.verify(mockRepository, Mockito.times(1)).update(mockUser);
    }

    @Test
    public void updateAsAdmin_Should_CallRepository_When_UserIsAuthorizedAndNewEmailIsUnique() {
        User mockUser = createMockUser();
        User mockAdmin = createMockAdmin();
        Mockito.when(mockRepository.getByEmail(mockUser.getEmail())).thenThrow(EntityNotFoundException.class);

        service.updateAsAdmin(mockUser, mockAdmin);

        Mockito.verify(mockRepository, Mockito.times(1)).update(mockUser);
    }

    @Test
    public void delete_Should_Throw_When_UserNotAdminOrRequester() {
        User mockUser = createMockUser();

        Assertions.assertThrows(AuthorizationException.class, () -> service.delete(mockUser, 2));
    }

    @Test
    public void delete_Should_CallRepository_When_UserIsAdmin() {
        User mockAdmin = createMockAdmin();

        service.delete(mockAdmin, 2);

        Mockito.verify(mockRepository, Mockito.times(1)).delete(2);
    }

    @Test
    public void delete_Should_CallRepository_When_UserIsRequester() {
        User mockUser = createMockUser();

        service.delete(mockUser, 1);

        Mockito.verify(mockRepository, Mockito.times(1)).delete(1);
    }
}
