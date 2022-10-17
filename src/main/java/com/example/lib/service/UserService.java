package com.example.lib.service;


import com.example.lib.dto.UserDto;
import com.example.lib.exception.GenericException;
import com.example.lib.model.Role;
import com.example.lib.model.User;
import com.example.lib.repository.UserRepository;
import com.example.lib.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional(rollbackOn = Exception.class)
    public User create(User user){
        return userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> GenericException.builder().httpStatus(HttpStatus.NOT_FOUND).errorMessage("user not found!").build());
    }

    public UserDto getUserDto(String username) {
        var user = findUserByUsername(username);
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public UserDto findInContextUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails details = (UserDetails) authentication.getPrincipal();
        if (details == null) {
            throw GenericException.builder().httpStatus(HttpStatus.UNAUTHORIZED).errorMessage("user not found!").build();
        }
        return getUserDto(details.getUsername());
    }

    public Boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

}
