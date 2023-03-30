package dev.be.moduleadmin.place.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.be.modulecore.domain.place.Place;
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

    private Long id;

    private String placeId;

    private String placeName;

    private String imageUrl;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

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
