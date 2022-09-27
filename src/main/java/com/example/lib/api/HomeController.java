package com.example.lib.api;
import com.example.lib.dto.UserDto;
import com.example.lib.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final AuthService authService;

    @GetMapping("/public")
    public String publicEndpoint() {
        return "public";
    }

    @GetMapping("/auth-required")
    public String testEndpoint() {
        return "auth-required";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin-pre-authorize")
    public String adminPreAuthorize() {
        return "admin";
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe() {
        return ResponseEntity.ok(authService.getAuthenticatedUser());
    }

}
