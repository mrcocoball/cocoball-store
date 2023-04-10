package dev.be.moduleapi.image.dto;

import dev.be.modulecore.domain.image.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ImageDto {

    @Schema(description = "uuid")
    private String uuid;

    @Schema(description = "파일명")
    private String fileName;

    @Schema(description = "이미지 순서")
    private int ord;

    public static ImageDto from(Image entity) {
        return ImageDto.builder()
                .uuid(entity.getUuid())
                .fileName(entity.getFileName())
                .ord(entity.getOrd())
                .build();
    }

}
