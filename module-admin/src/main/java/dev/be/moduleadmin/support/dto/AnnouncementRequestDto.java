package dev.be.moduleadmin.support.dto;

import dev.be.modulecore.domain.support.Announcement;
import dev.be.modulecore.domain.support.AnnouncementCategory;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Slf4j(topic = "DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementRequestDto {

    @NotNull(message = "내용이 입력되어야 합니다")
    @NotEmpty(message = "내용이 입력되어야 합니다")
    private String title;

    @NotNull(message = "내용이 입력되어야 합니다")
    @NotEmpty(message = "내용이 입력되어야 합니다")
    private String description;

    private Long categoryId;

    public Announcement toEntity(String title, String description, AnnouncementCategory announcementCategory) {

        return Announcement.of(
                title,
                description,
                announcementCategory
        );

    }

}
