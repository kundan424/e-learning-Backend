package com.example.elearning_platform.service;

import com.example.elearning_platform.dto.AnnouncementDTO;
import com.example.elearning_platform.entity.Announcement;
import com.example.elearning_platform.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private  final AnnouncementRepository announcementRepository;

    public AnnouncementDTO getAnnouncementDetails ( Long announcementId){
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));

        return AnnouncementDTO.fromEntity(announcement);
    }
}
