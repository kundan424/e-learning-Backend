package com.example.elearning_platform.controller;

import com.example.elearning_platform.dto.UserDTO;
import com.example.elearning_platform.entity.Role;
import com.example.elearning_platform.repository.UserRepository;
import com.example.elearning_platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/contacts")
    public ResponseEntity<?> getContacts (Authentication authentication){
        String myUsername = authentication.getName();

        List<UserDTO> contacts = userService.getContactsForUser(myUsername);
        return  ResponseEntity.ok(contacts);
    }

}
