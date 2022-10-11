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

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    PasswordEncoder encoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User create(User user) {
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
        return getUserDto(details.getUsername());
    }

    public UserDto signup(SignUpRequest signUpRequest){
        var user = findUserByUsername(signUpRequest.getUsername());

        if(user == null){

            user = User.builder()
                    .username(signUpRequest.getUsername())
                    .password(encoder.encode(signUpRequest.getPassword()))
                    .role(Role.USER)
                    .build();

            User fromDb = null;

            try {
                fromDb = create(user);
            } catch (DataAccessException ex) {
                throw GenericException.builder().httpStatus(HttpStatus.BAD_REQUEST)
                        .errorMessage("user cannot created!").build();
            }

            return UserDto.builder()
                    .id(fromDb.getId())
                    .username(fromDb.getUsername())
                    .role(fromDb.getRole())
                    .build();

        }

        throw GenericException.builder().httpStatus(HttpStatus.FOUND)
                .errorMessage("Username" + signUpRequest.getUsername() + "is already used").build();
    }

}
