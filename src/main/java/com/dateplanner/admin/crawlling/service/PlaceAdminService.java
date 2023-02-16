package com.dateplanner.admin.crawlling.service;

import com.dateplanner.admin.crawlling.dto.PlaceStatusDto;
import com.dateplanner.api.PaginationService;
import com.dateplanner.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlaceAdminService {

    private final PlaceRepository placeRepository;
    private final PaginationService paginationService;


    public Page<PlaceStatusDto> getImageUrlNullPlacesV1(Pageable pageable) {

        return paginationService.listToPage(placeRepository.findByImageUrlIsNull().stream().map(PlaceStatusDto::from).collect(Collectors.toList()), pageable);
    }

    public Page<PlaceStatusDto> getImageUrlNotExistPlacesV1(Pageable pageable) {

        String condition = "크롤링 결과 이미지 없음";

        return paginationService.listToPage(placeRepository.findByImageUrlIs(condition).stream().map(PlaceStatusDto::from).collect(Collectors.toList()), pageable);
    }

}
