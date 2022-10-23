package com.example.lib.repository;

import com.example.lib.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest extends BaseRepositoryTests{

    @Autowired
    private UserRepository userRepository;

    @Test
    void itShouldFindUserByName_WhenUserNameExists() throws Exception {

        // given - precondition or setup
        String username = "username";

        User expected = User.builder()
                .username(username)
                .password("password")
                .build();

        userRepository.save(expected);

        // when -  action or the behaviour that we are going test
        User actual = userRepository.findByUsername(username).get();

        // then - verify the output
        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test
    void itShouldReturnTrue_WhenUserNameExists() throws Exception {

        // given - precondition or setup
        String username = "username";

        User expected = User.builder()
                .username(username)
                .password("password")
                .build();

        userRepository.save(expected);

        // when -  action or the behaviour that we are going test
        boolean actual = userRepository.existsByUsername(username);

        // then - verify the output
        assertEquals(true, actual);
    }
}