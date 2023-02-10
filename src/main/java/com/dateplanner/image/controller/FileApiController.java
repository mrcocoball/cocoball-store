package com.dateplanner.image.controller;

import com.dateplanner.image.dto.UploadFileDto;
import com.dateplanner.image.dto.UploadResultDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "7. [이미지 첨부, 삭제] FileApiController - 이미지, 파일 첨부 기능 API")
@RequiredArgsConstructor
@RestController
public class FileApiController {

    @Value("${com.dateplanner.upload.path}")
    private String uploadPath;


    @Operation(summary = "[POST] 파일 등록하기, multipart form",
            description = "파일을 첨부합니다. 첨부된 파일을 받은 서버에서는 uuid와 원본 파일명을 토대로 새 파일명(링크)을 만들고 그 파일명으로 서버에 저장합니다.")
    @PostMapping(value = "/api/v1/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDto> upload(@Parameter(description = "업로드 파일",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = UploadFileDto.class))) UploadFileDto uploadFileDto) {

        log.info("[FileApiController upload] file upload start...");

        if (uploadFileDto.getFiles() != null) {

            final List<UploadResultDto> list = new ArrayList<>();

            uploadFileDto.getFiles().forEach(multipartFile -> {

                String originalName = multipartFile.getOriginalFilename();
                log.info(originalName);

                String uuid = UUID.randomUUID().toString();

                Path savePath = Paths.get(uploadPath, "s_" + uuid + "_" + originalName);

                boolean image = false;

                // 실제 저장 작업

                try {
                    multipartFile.transferTo(savePath);

                    //이미지 파일의 종류라면
                    if (Files.probeContentType(savePath).startsWith("image")) {

                        log.info("[FileApiController upload] this file is a image");
                        image = true;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                list.add(UploadResultDto.builder()
                        .uuid(uuid)
                        .fileName(originalName)
                        .img(image).build()
                );

            });

            log.info("[FileApiController upload] file upload complete");

            return list;
        }

        log.info("[FileApiController upload] file is null");

        return null;
    }


    @Operation(summary = "[GET] 첨부 파일 조회",
            description = "파일명(위에서 저장된 파일명, 링크)으로 서버에 저장되어 있는 파일을 검색해서 결과를 반환합니다.")
    @GetMapping("/api/v1/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@Parameter(description = "파일명") @PathVariable String fileName) {

        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }


    @Operation(summary = "[DELETE] 파일 삭제",
            description = "파일명(위에서 저장된 파일명, 링크)으로 서버에 저장되어 있는 파일을 삭제합니다.")
    @DeleteMapping("/api/v1/remove/{fileName}")
    public Map<String, Boolean> removeFile(@Parameter(description = "파일명") @PathVariable String fileName) {

        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            //섬네일이 존재한다면
            if (contentType.startsWith("image")) {
                File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                thumbnailFile.delete();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        resultMap.put("result", removed);

        return resultMap;
    }


}
