package com.dateplanner.admin.consumer.dto;

import com.dateplanner.admin.consumer.entity.AnnouncementCategory;
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
public class AnnouncementCategoryRequestDto {

    @NotNull(message = "제목이 입력되어야 합니다")
    @NotEmpty(message = "제목이 입력되어야 합니다")
    private String categoryName;

    public AnnouncementCategory toEntity(String categoryName) {

        return AnnouncementCategory.of(categoryName);

    }

}
