package dev.be.moduleapi.image.service;

import dev.be.moduleapi.review.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional
@Service
public class FileService {

    @Value("${com.dateplanner.upload.path}")
    private String uploadPath;

    public void removeFilesInDto(ReviewDto dto) {

        log.info("[FileService removeFilesInDto] get Files and Remove Files in dto");

        List<String> fileNames = dto.getFileNames();
        if (fileNames != null && fileNames.size() > 0) {
            removeFiles(fileNames);
        }

        log.info("[FileService removeFilesInDto] complete");
    }

    public void removeFiles(List<String> files) {

        for (String fileName : files) {

            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
            String resourceName = resource.getFilename();

            try {
                resource.getFile().delete();

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
