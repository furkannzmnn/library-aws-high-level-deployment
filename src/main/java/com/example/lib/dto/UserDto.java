package com.example.lib.dto;

import com.example.lib.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {

    private String username;
    private Role role;
    private Long id;


}
