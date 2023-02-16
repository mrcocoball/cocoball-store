package com.dateplanner.admin.crawlling.dto;

import com.dateplanner.place.entity.Place;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceStatusDto {

    @Schema(description = "장소 ID")
    private Long id;

    @Schema(description = "장소 ID (카카오, 대부분 해당 ID 사용)")
    private String placeId;

    @Schema(description = "장소명")
    private String placeName;

    @Schema(description = "이미지 URL")
    private String imageUrl;

    @Schema(description = "장소 설명")
    private String description;

    @Schema(description = "장소가 생성된 날짜")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @Schema(description = "장소가 수정된 날짜")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime modifiedAt;

    public static PlaceStatusDto from(Place entity) {

        return PlaceStatusDto.builder()
                .id(entity.getId())
                .placeName(entity.getPlaceName())
                .placeId(entity.getPlaceId())
                .imageUrl(entity.getImageUrl())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }

}
