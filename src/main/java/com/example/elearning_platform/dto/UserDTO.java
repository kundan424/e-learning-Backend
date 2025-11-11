package com.example.elearning_platform.dto;

import com.example.elearning_platform.entity.User;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String role;

    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().name()); // Assumes getRole() returns an Enum
        return dto;
    }
}
