package com.example.elearning_platform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    // --- Relationships ---

    // 1. The instructor who created this course
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    // 2. The lessons that make up this course
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Lesson> lessons = new HashSet<>();

    // 3. The students enrolled in this course (inverse of User.enrolledCourses)
    @ManyToMany(mappedBy = "enrolledCourses", fetch = FetchType.LAZY)
    private Set<User> students = new HashSet<>();

}