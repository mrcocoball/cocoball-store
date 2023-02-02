package com.dateplanner.image.dto;

import com.dateplanner.image.entity.Image;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ImageDto {

    private String uuid;

    private String fileName;

    private int ord;


    public static ImageDto of(String uuid, String fileName, int ord) {
        return new ImageDto(uuid, fileName, ord);
    }

    public static ImageDto from(Image entity) {
        return ImageDto.builder()
                .uuid(entity.getUuid())
                .fileName(entity.getFileName())
                .ord(entity.getOrd())
                .build();
    }

}
