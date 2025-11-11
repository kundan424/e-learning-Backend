package com.example.elearning_platform.service;

import com.example.elearning_platform.dto.UserDTO;
import com.example.elearning_platform.entity.Course;
import com.example.elearning_platform.entity.Role;
import com.example.elearning_platform.entity.User;
import com.example.elearning_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public List<UserDTO> getContactsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role myRole = user.getRole();
        Set<User> contacts = new HashSet<>();

        if (myRole == Role.STUDENT) {
            // Students see all instructors
            contacts.addAll(userRepository.findByRole(Role.INSTRUCTOR));
        } else if (myRole == Role.INSTRUCTOR) {
            // Instructors see all students enrolled in their courses
            // This is safe because the method is @Transactional
            Set<Course> myCourses = user.getCreatedCourses();
            for (Course course : myCourses) {
                contacts.addAll(course.getStudents());
            }
        }

        // Convert the Set<User> to a List<UserDTO> and filter out yourself
        return contacts.stream()
                .filter(contact -> !contact.getUsername().equals(username))
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
