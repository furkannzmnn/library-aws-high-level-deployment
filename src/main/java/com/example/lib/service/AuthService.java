package com.example.lib.service;

import com.example.lib.dto.ErrorCode;
import com.example.lib.dto.TokenResponseDTO;
import com.example.lib.dto.UserDto;
import com.example.lib.dto.request.LoginRequest;
import com.example.lib.dto.request.SignUpRequest;
import com.example.lib.exception.GenericException;
import com.example.lib.model.Role;
import com.example.lib.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;

@Service
@RequiredArgsConstructor
public class AuthService{

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
                    .user(userService.findUser(loginRequest.getUsername()))
                    .build();
        } catch (final BadCredentialsException badCredentialsException) {
            throw GenericException.builder().httpStatus(HttpStatus.NOT_FOUND).errorCode(ErrorCode.USER_NOT_FOUND).errorMessage("Invalid Username or Password").build();
        }
    }


    @Transactional
    public UserDto signup(SignUpRequest signUpRequest){
        var isAllReadyRegistered = userService.existsByUsername(signUpRequest.getUsername());

        if(isAllReadyRegistered) {
            throw GenericException.builder().httpStatus(HttpStatus.FOUND)
                    .errorMessage("Username" + signUpRequest.getUsername() + "is already used").build();
        }

        var user = User.builder()
                .username(signUpRequest.getUsername())
                .password(encoder.encode(signUpRequest.getPassword()))
                .role(Role.USER)
                .build();

        User fromDb = userService.create(user);

        return UserDto.builder()
                .id(fromDb.getId())
                .username(fromDb.getUsername())
                .role(fromDb.getRole())
                .build();

    }
}
