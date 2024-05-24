package org.moviematchers.moviematch.service;

import jakarta.transaction.Transactional;
import org.moviematchers.moviematch.entity.MovieUser;
import org.moviematchers.moviematch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public List<MovieUser> getUsers() {
        return userRepository.findAll();
    }

    public boolean addUser(MovieUser movieUser) {
        if (!userRepository.existsMovieUserByUserName(movieUser.getUserName())) {
            try {
                String password = movieUser.getUserPassword();
                String encodedPassword = bCryptPasswordEncoder.encode(password);
                movieUser.setUserPassword(encodedPassword);

                userRepository.save(movieUser);
                return true;
            } catch (Exception e) {
                return false;
            }
        }else return false;
    }

    @Transactional
    public boolean changePassword(String login, String value) {
        if (userRepository.existsMovieUserByUserName(login)) {
            try {
                MovieUser movieUser = userRepository.findByUserName(login);
                if (value != null && value.length() > 0 && !Objects.equals(movieUser.getUserPassword(), value)) {
                    movieUser.setUserPassword(bCryptPasswordEncoder.encode(value));
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }else return false;
    }
}
