package com.example.lib.service;

import com.example.lib.dto.UserDto;
import com.example.lib.exception.GenericException;
import com.example.lib.model.Role;
import com.example.lib.model.User;
import com.example.lib.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest extends BaseServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;


    @Test
    void itShouldCreateUser() {

        // given - precondition or setup
        User user = User.builder()
                .username("username")
                .password("password")
                .build();

        // when -  action or the behaviour that we are going test
        when(userRepository.save(user)).thenReturn(user);

        User actual = userService.create(user);

        // then - verify the output
        assertEquals(user, actual);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void itShouldReturnUserByGivenUsername_WhenUserExist() {

        // given - precondition or setup
        String username = "username";

        User expected = User.builder()
                .username(username)
                .password("password")
                .build();

        // when -  action or the behaviour that we are going test
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expected));

        User actual = userService.findUserByUsername(username);

        // then - verify the output
        assertEquals(expected, actual);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void itShouldThrowError_WhenUsernameNotExists() {

        // given - precondition or setup
        GenericException expectedError = GenericException.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .errorMessage("user not found!").build();

        // when -  action or the behaviour that we are going test
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        GenericException actual = assertThrows(GenericException.class, () -> userService.findUserByUsername(Mockito.anyString()));

        // then - verify the output
        verify(userRepository).findByUsername(Mockito.anyString());
        assertEquals(expectedError.getErrorMessage(), actual.getErrorMessage());

    }

    @Test
    void itShouldReturnTrue_WhenUserNameExists() {

        // given - precondition or setup
        boolean isExists = true;
        String username = "username";

        // when -  action or the behaviour that we are going test
        when(userRepository.existsByUsername(username)).thenReturn(isExists);

        boolean actual = userRepository.existsByUsername(username);

        // then - verify the output
        assertEquals(isExists, actual);
        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void itShouldFindInContextUser(){

        // given - precondition or setup
        User user = User.builder()
                .username("username")
                .password("password")
                .role(Role.USER)
                .build();

        UserDto expected = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();

        var roles = Stream.of(user.getRole())
                .map(x -> new SimpleGrantedAuthority(x.name()))
                .collect(Collectors.toList());

        UserDetails details = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        // when -  action or the behaviour that we are going test
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(details);
        when(userRepository.findByUsername(details.getUsername())).thenReturn(Optional.ofNullable(user));

        SecurityContextHolder.setContext(securityContext);

        // then - verify the output
        UserDto actual = userService.findUserInContext();
        assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());

    }

}