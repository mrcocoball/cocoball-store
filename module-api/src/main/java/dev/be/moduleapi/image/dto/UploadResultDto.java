package dev.be.moduleapi.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UploadResultDto {

    @Schema(description = "uuid")
    private String uuid;

    @Schema(description = "파일명")
    private String fileName;

    @Schema(description = "이미지 파일 여부")
    private boolean img;

    public String getLink(){

        if(img){
            return "s_"+ uuid +"_"+fileName; //이미지인 경우 섬네일
        }else {
            return uuid+"_"+fileName;
        }
    }
}
