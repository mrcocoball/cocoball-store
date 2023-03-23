package com.dateplanner.support.dto;

import com.dateplanner.admin.consumer.entity.Announcement;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementDto {

    private Long id;

    private String title;

    private String description;

    private Long categoryId;

    private String categoryName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    public static AnnouncementDto from(Announcement entity) {
        return AnnouncementDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .categoryId(entity.getAnnouncementCategory().getId())
                .categoryName(entity.getAnnouncementCategory().getCategoryName())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
