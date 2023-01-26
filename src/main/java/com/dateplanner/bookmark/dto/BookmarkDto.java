package com.dateplanner.bookmark.dto;

import com.dateplanner.bookmark.entity.Bookmark;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class BookmarkDto {

    private Long id;
    private String uid;
    private String placeId;
    private String placeName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    public static BookmarkDto from(Bookmark entity) {
        return BookmarkDto.builder()
                .id(entity.getId())
                .uid(entity.getUser().getUid())
                .placeId(entity.getPlace().getPlaceId())
                .placeName(entity.getPlace().getPlaceName())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
