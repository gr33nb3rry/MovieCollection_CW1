package org.moviematchers.moviematch.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.moviematchers.moviematch.entity.MovieUser;
import org.moviematchers.moviematch.entity.UserMovieCollection;
import org.moviematchers.moviematch.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserService service;
    @Mock
    UserRepository repository;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    MovieUser user;
    @BeforeEach
    public void init() {
        user = new MovieUser(0L, "coca", "cola", new ArrayList<UserMovieCollection>());
    }

    @Test
    void getUsers() {
        // GIVEN
        when(repository.findAll()).thenReturn(Arrays.asList(user, user));
        // WHEN
        List<MovieUser> result = service.getUsers();
        // THEN
        verify(repository, Mockito.times(1)).findAll();
        verifyNoMoreInteractions(repository);
        Assertions.assertThat(result).isEqualTo(Arrays.asList(user, user));
    }

    @Test
    void addUser_success() {
        // GIVEN
        when(repository.existsMovieUserByUserName(user.getUserName())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(user.getUserPassword())).thenReturn(user.getUserPassword());
        when(repository.save(user)).thenReturn(user);
        // WHEN
        boolean result = service.addUser(user);
        // THEN
        verify(repository, Mockito.times(1)).existsMovieUserByUserName(user.getUserName());
        verify(repository, Mockito.times(1)).save(user);
        verifyNoMoreInteractions(repository);
        Assertions.assertThat(result).isTrue();
    }
    @Test
    void addUser_failed() {
        // GIVEN
        when(repository.existsMovieUserByUserName(user.getUserName())).thenReturn(true);
        // WHEN
        boolean result = service.addUser(user);
        // THEN
        verify(repository, Mockito.times(1)).existsMovieUserByUserName(user.getUserName());
        verifyNoMoreInteractions(repository);
        Assertions.assertThat(result).isFalse();
    }
    @Test
    void addUser_failed_encoder() {
        // GIVEN
        when(repository.existsMovieUserByUserName(user.getUserName())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(user.getUserPassword())).thenThrow(new IllegalArgumentException("message"));
        // WHEN
        boolean result = service.addUser(user);
        // THEN
        verify(repository, Mockito.times(1)).existsMovieUserByUserName(user.getUserName());
        verifyNoMoreInteractions(repository);
        Assertions.assertThat(result).isFalse();
    }
    @Test
    void addUser_failed_save() {
        // GIVEN
        when(repository.existsMovieUserByUserName(user.getUserName())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(user.getUserPassword())).thenReturn(user.getUserPassword());
        when(repository.save(user)).thenThrow(new DataIntegrityViolationException("message"));
        // WHEN
        boolean result = service.addUser(user);
        // THEN
        verify(repository, Mockito.times(1)).existsMovieUserByUserName(user.getUserName());
        verify(repository, Mockito.times(1)).save(user);
        verifyNoMoreInteractions(repository);
        Assertions.assertThat(result).isFalse();
    }

    @Test
    void changePassword_success() {
        // GIVEN
        String new_password = "pepsi";
        MovieUser new_user = new MovieUser(0L, "coca", new_password, new ArrayList<UserMovieCollection>());
        when(repository.existsMovieUserByUserName(user.getUserName())).thenReturn(true);
        when(repository.findByUserName(user.getUserName())).thenReturn(user);
        when(bCryptPasswordEncoder.encode(new_password)).thenReturn(new_password);
        // WHEN
        boolean result = service.changePassword(user.getUserName(), new_password);
        // THEN
        verify(repository, Mockito.times(1)).existsMovieUserByUserName(user.getUserName());
        verify(repository, Mockito.times(1)).findByUserName(user.getUserName());
        verifyNoMoreInteractions(repository);
        Assertions.assertThat(result).isTrue();
    }
    @Test
    void changePassword_failed() {
        // GIVEN
        String new_password = "pepsi";
        when(repository.existsMovieUserByUserName(user.getUserName())).thenReturn(false);
        // WHEN
        boolean result = service.changePassword(user.getUserName(), new_password);
        // THEN
        verify(repository, Mockito.times(1)).existsMovieUserByUserName(user.getUserName());
        verifyNoMoreInteractions(repository);
        Assertions.assertThat(result).isFalse();
    }
    @Test
    void changePassword_failed_find() {
        // GIVEN
        String new_password = "pepsi";
        MovieUser new_user = new MovieUser(0L, "coca", new_password, new ArrayList<UserMovieCollection>());
        when(repository.existsMovieUserByUserName(user.getUserName())).thenReturn(true);
        when(repository.findByUserName(user.getUserName())).thenThrow(new IllegalArgumentException("message"));
        // WHEN
        boolean result = service.changePassword(user.getUserName(), new_password);
        // THEN
        verify(repository, Mockito.times(1)).existsMovieUserByUserName(user.getUserName());
        verify(repository, Mockito.times(1)).findByUserName(user.getUserName());
        verifyNoMoreInteractions(repository);
        Assertions.assertThat(result).isFalse();
    }
    @Test
    void changePassword_failed_samePassword() {
        // GIVEN
        String new_password = "cola";
        MovieUser new_user = new MovieUser(0L, "coca", new_password, new ArrayList<UserMovieCollection>());
        when(repository.existsMovieUserByUserName(user.getUserName())).thenReturn(true);
        when(repository.findByUserName(user.getUserName())).thenReturn(user);
        // WHEN
        boolean result = service.changePassword(user.getUserName(), new_password);
        // THEN
        verify(repository, Mockito.times(1)).existsMovieUserByUserName(user.getUserName());
        verify(repository, Mockito.times(1)).findByUserName(user.getUserName());
        verifyNoMoreInteractions(repository);
        Assertions.assertThat(result).isFalse();
    }
}