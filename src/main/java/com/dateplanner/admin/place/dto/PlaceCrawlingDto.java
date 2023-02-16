package com.dateplanner.admin.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceCrawlingDto {

    @Schema(description = "장소 ID (카카오, 대부분 해당 ID 사용)")
    private String placeId;

    @Schema(description = "이미지 URL (기본값은 'NOT EXISTS'")
    private String imageUrl = "NOT EXISTS";

    @Schema(description = "장소 태그")
    private List<String> tags = new ArrayList<>();

    public void addTag(String tag) {
        tags.add(tag);
    }

}
