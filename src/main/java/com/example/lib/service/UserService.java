package com.example.lib.service;


import com.example.lib.dto.UserDto;
import com.example.lib.exception.GenericException;
import com.example.lib.model.User;
import com.example.lib.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
    public User create(User user){
        return userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(notFoundUser(HttpStatus.NOT_FOUND));
    }

    public UserDto findUser(String username) {
        var user = findUserByUsername(username);
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public UserDto findUserInContext() {
        final Authentication authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).orElseThrow(notFoundUser(HttpStatus.UNAUTHORIZED));
        final UserDetails details = Optional.ofNullable((UserDetails) authentication.getPrincipal()).orElseThrow(notFoundUser(HttpStatus.UNAUTHORIZED));
        return findUser(details.getUsername());
    }

    private static Supplier<GenericException> notFoundUser(HttpStatus unauthorized) {
        return () -> GenericException.builder().httpStatus(unauthorized).errorMessage("user not found!").build();
    }

    public Boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

}
