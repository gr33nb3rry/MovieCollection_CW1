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
import org.moviematchers.moviematch.repository.CollectionRepository;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class CollectionServiceTest {
    @InjectMocks
    CollectionService service;
    @Mock
    CollectionRepository repository;
    UserMovieCollection collection;
    MovieUser user;
    @BeforeEach
    public void init() {
        user = new MovieUser(0L, "coca", "cola", new ArrayList<UserMovieCollection>());
        collection = new UserMovieCollection(user, "Interstellar", 9.7);
    }
    @Test
    void addCollection_success() {
        // GIVEN
        when(repository.save(collection)).thenReturn(collection);
        // WHEN
        boolean result = service.addCollection(collection);
        // THEN
        verify(repository, Mockito.times(1)).save(collection);
        verifyNoMoreInteractions(repository);
        Assertions.assertThat(result).isTrue();
    }
    @Test
    void addCollection_failed() {
        // GIVEN
        when(repository.save(collection)).thenThrow(new DataIntegrityViolationException("message"));
        // WHEN
        boolean result = service.addCollection(collection);
        // THEN
        verify(repository, Mockito.times(1)).save(collection);
        verifyNoMoreInteractions(repository);
        Assertions.assertThat(result).isFalse();
    }

    @Test
    void getAllCollectionsOfUser_success() {
        // GIVEN
        when(repository.findByUserIDUserID(user.getUserID())).thenReturn(Arrays.asList(collection, collection));
        // WHEN
        List<UserMovieCollection> result = service.getAllCollectionsOfUser(user.getUserID());
        // THEN
        verify(repository, Mockito.times(1)).findByUserIDUserID(user.getUserID());
        verifyNoMoreInteractions(repository);
        Assertions.assertThat(result).isEqualTo(Arrays.asList(collection, collection));
    }

}