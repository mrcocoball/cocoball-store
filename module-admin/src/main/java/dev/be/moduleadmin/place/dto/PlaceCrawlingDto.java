package dev.be.moduleadmin.place.dto;

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

    private String placeId;

    private String imageUrl = "NOT EXISTS";

    private double reviewScore;

    private long reviewCount;

    private List<String> tags = new ArrayList<>();

    public void addTag(String tag) {
        tags.add(tag);
    }

}
