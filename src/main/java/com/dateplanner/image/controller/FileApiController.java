package com.dateplanner.image.controller;

import com.dateplanner.image.dto.UploadFileDto;
import com.dateplanner.image.dto.UploadResultDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "FileApiController - 이미지, 파일 첨부 기능 API")
@RequiredArgsConstructor
@RestController
public class FileApiController {

    @Value("${com.dateplanner.upload.path}")
    private String uploadPath;


    @Operation(summary = "파일 등록하기, multipart form", description = "[POST] 파일 등록")
    @PostMapping(value = "/api/v1/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDto> upload(@Parameter(description = "업로드 파일") UploadFileDto uploadFileDto) {

        log.info("[FileApiController upload] file upload start...");

        if (uploadFileDto.getFiles() != null) {

            final List<UploadResultDto> list = new ArrayList<>();

            uploadFileDto.getFiles().forEach(multipartFile -> {

                String originalName = multipartFile.getOriginalFilename();
                log.info(originalName);

                String uuid = UUID.randomUUID().toString();

                Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);

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


    @Operation(summary = "첨부된 파일 조회", description = "[GET] 첨부 파일 조회")
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


    @Operation(summary = "첨부된 파일 삭제", description = "[DELETE] 파일 삭제")
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
