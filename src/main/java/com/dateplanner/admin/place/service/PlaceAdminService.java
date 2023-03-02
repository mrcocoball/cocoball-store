package com.dateplanner.admin.place.service;

import com.dateplanner.admin.place.dto.PlaceCrawlingDto;
import com.dateplanner.admin.place.dto.PlaceStatusDto;
import com.dateplanner.common.pagination.PaginationService;
import com.dateplanner.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional
@Service
public class PlaceAdminService {

    private final PlaceRepository placeRepository;
    private final PaginationService paginationService;
    private final static String NOT_EXIST_IMAGE = "NOT EXISTS";


    @Transactional(readOnly = true)
    public Page<PlaceStatusDto> getImageUrlNullPlacesV1(Pageable pageable) {

        return paginationService.listToPage(placeRepository.findByImageUrlIsNull().stream().map(PlaceStatusDto::from).collect(Collectors.toList()), pageable);
    }


    @Transactional(readOnly = true)
    public Page<PlaceStatusDto> getImageUrlNotExistPlacesV1(Pageable pageable) {

        return paginationService.listToPage(placeRepository.findByImageUrlIs(NOT_EXIST_IMAGE).stream().map(PlaceStatusDto::from).collect(Collectors.toList()), pageable);
    }


    public Long updatePlacesV1(List<PlaceCrawlingDto> dtos) {

        // 저장 시간 비교 계산용 측정
        long beforeTime = System.currentTimeMillis();

        Long count = 0L;

        for (PlaceCrawlingDto dto : dtos) {
            List<String> tags = dto.getTags();
            StringJoiner stringJoiner = new StringJoiner(", ");
            stringJoiner.setEmptyValue("");
            if (!ObjectUtils.isEmpty(tags) || tags != null) {
                for (String tag : tags) {
                    stringJoiner.add(tag);
                }
            }
            String description = String.valueOf(stringJoiner);
            if (description.equals("")) {description = null;}
            placeRepository.updateImageUrlAndDescription(dto.getPlaceId(), dto.getImageUrl(), description);
            count += 1L;
            log.info("[PlaceCrawlingService searchAndCrawling] - {} of {} complete", count, dtos.size());
        }

        // 저장 시간 비교 계산용 측정
        long afterTime = System.currentTimeMillis();
        log.info("elapsed time : " + (afterTime - beforeTime));

        return count;
    }

}
