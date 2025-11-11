package com.example.elearning_platform.repository;

import com.example.elearning_platform.entity.Role;
import com.example.elearning_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {


    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);
}
