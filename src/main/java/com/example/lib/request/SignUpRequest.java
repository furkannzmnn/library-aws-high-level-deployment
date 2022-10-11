package com.example.lib.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignUpRequest {

    private String username;
    private String password;
    private String role;
}
