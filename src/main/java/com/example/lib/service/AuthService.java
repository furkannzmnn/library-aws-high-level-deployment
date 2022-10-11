package com.example.lib.service;

import com.example.lib.dto.ErrorCode;
import com.example.lib.dto.TokenResponseDTO;
import com.example.lib.dto.UserDto;
import com.example.lib.exception.GenericException;
import com.example.lib.model.Book;
import com.example.lib.model.Role;
import com.example.lib.model.User;
import com.example.lib.request.LoginRequest;
import com.example.lib.request.SignUpRequest;
import com.example.lib.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;

    private final PasswordEncoder encoder;


    public TokenResponseDTO login(LoginRequest loginRequest) {
        try {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            return TokenResponseDTO
                    .builder()
                    .accessToken(tokenService.generateToken(auth))
                    .user(userService.getUserDto(loginRequest.getUsername()))
                    .build();
        } catch (final BadCredentialsException badCredentialsException) {
            throw GenericException.builder().httpStatus(HttpStatus.NOT_FOUND).errorCode(ErrorCode.USER_NOT_FOUND).errorMessage("Invalid Username or Password").build();
        }
    }

    public UserDto getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserDto(username);
    }


    public Object signup(SignUpRequest signUpRequest){

        if(userService.existsByUsername(signUpRequest.getUsername())){
            return new MessageResponse("Error: Username is already taken!");
        }

        var user = User.builder()
                .username(signUpRequest.getUsername())
                .password(encoder.encode(signUpRequest.getPassword()))
                .build();

        String signUpRole = signUpRequest.getRole();

        switch(signUpRole) {
            case "ADMIN":
                user.setRole(Role.ADMIN);
                break;
            default:
                user.setRole(Role.USER);
                break;
        };

        final User fromDb = userService.create(user);

        return UserDto.builder()
                .id(fromDb.getId())
                .username(fromDb.getUsername())
                .role(fromDb.getRole())
                .build();

    }
}
