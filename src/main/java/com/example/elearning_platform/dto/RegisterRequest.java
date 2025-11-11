package com.example.elearning_platform.dto;

import com.example.elearning_platform.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class RegisterRequest {

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Password is required")
    private Role role;

}
