package com.example.elearning_platform.controller;

import com.example.elearning_platform.dto.AnnouncementDTO;
import com.example.elearning_platform.entity.Announcement;
import com.example.elearning_platform.repository.AnnouncementRepository;
import com.example.elearning_platform.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementService announcementService;

    // to get the FULL announcement content
    @GetMapping("/{announcementId}")
    public ResponseEntity<AnnouncementDTO> getAnnouncementContent(@PathVariable Long announcementId) {
        AnnouncementDTO dto = announcementService.getAnnouncementDetails(announcementId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Announcement>> getAnnouncementsForCourse(@PathVariable Long courseId) {
        List<Announcement> history = announcementRepository.findByCourseIdOrderByCreatedAtDesc(courseId);
        return ResponseEntity.ok(history);
    }

}
