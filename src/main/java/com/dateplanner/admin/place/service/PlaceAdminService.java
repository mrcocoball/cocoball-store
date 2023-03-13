package com.dateplanner.admin.place.service;

import com.dateplanner.admin.place.dto.*;
import com.dateplanner.admin.place.repository.PlaceAdminCustomRepository;
import com.dateplanner.admin.place.repository.PlaceAdminRepository;
import com.dateplanner.advice.exception.PlaceNotFoundApiException;
import com.dateplanner.common.pagination.PaginationService;
import com.dateplanner.kakao.dto.DocumentDto;
import com.dateplanner.kakao.service.KakaoAddressSearchService;
import com.dateplanner.place.entity.Place;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional
@Service
public class PlaceAdminService {

    private final PlaceAdminRepository placeAdminRepository;
    private final PlaceAdminCustomRepository placeAdminCustomRepository;
    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final PaginationService paginationService;
    private final static String NOT_EXIST_IMAGE = "NOT EXISTS";


    /**
     * 장소 크롤링 및 업데이트 관련
     */

    @Transactional(readOnly = true)
    public Page<PlaceStatusDto> getImageUrlNullPlacesV1(Pageable pageable) {

        return paginationService.listToPage(placeAdminRepository.findByImageUrlIsNull().stream().map(PlaceStatusDto::from).collect(Collectors.toList()), pageable);
    }


    @Transactional(readOnly = true)
    public Page<PlaceStatusDto> getImageUrlNotExistPlacesV1(Pageable pageable) {

        return paginationService.listToPage(placeAdminRepository.findByImageUrlIs(NOT_EXIST_IMAGE).stream().map(PlaceStatusDto::from).collect(Collectors.toList()), pageable);
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
            placeAdminRepository.updateImageUrlAndDescription(dto.getPlaceId(), dto.getImageUrl(), description);
            count += 1L;
            log.info("[PlaceCrawlingService searchAndCrawling] - {} of {} complete", count, dtos.size());
        }

        // 저장 시간 비교 계산용 측정
        long afterTime = System.currentTimeMillis();
        log.info("elapsed time : " + (afterTime - beforeTime));

        return count;
    }


    /**
     * 장소 CRUD 관련
     */

    @Transactional(readOnly = true)
    public List<PlaceAdminDetailDto> getPlaceList(String region1, String region2, String region3,
                                                  String categoryId, Long id, String placeId, String placeName,
                                                  Long reviewCount, LocalDate startDate, LocalDate targetDate) {

        return placeAdminCustomRepository.placeList(region1, region2, region3, categoryId, id, placeId, placeName, reviewCount, startDate, targetDate);

    }

    @Transactional(readOnly = true)
    public PlaceAdminDetailDto getPlace(Long id) {

        return placeAdminRepository.findById(id).map(PlaceAdminDetailDto::from).orElseThrow(PlaceNotFoundApiException::new);

    }

    public Long savePlace(PlaceRequestDto dto) {

        DocumentDto addressDto = kakaoAddressSearchService.requestAddressSearch(dto.getAddressName()).getDocumentList().get(0);

        Place place = placeAdminRepository.save(dto.toEntity(
                dto.getCategoryGroupId(), dto.getPlaceName(), dto.getPlaceId(), dto.getPlaceUrl(), dto.getAddressName(),
                dto.getRoadAddressName(), dto.getRegion2DepthName(), dto.getRegion2DepthName(), dto.getRegion3DepthName(),
                Double.valueOf(addressDto.getLongitude()), Double.valueOf(addressDto.getLatitude()), dto.getImageUrl(), dto.getDescription()
        ));

        return place.getId();

    }

    public Long updatePlace(PlaceModifyRequestDto dto) {

        Place place = placeAdminRepository.findById(dto.getId()).orElseThrow(PlaceNotFoundApiException::new);

        if (place.getPlaceId() != dto.getPlaceId()) place.changePlaceId(dto.getPlaceId());
        if (place.getPlaceName() != dto.getPlaceName()) place.changePlaceName(dto.getPlaceName());
        if (place.getPlaceUrl() != dto.getPlaceUrl()) place.changePlaceUrl(dto.getPlaceUrl());
        if (place.getAddressName() != dto.getAddressName()) place.changeAddressName(dto.getAddressName());
        if (place.getRoadAddressName() != dto.getRoadAddressName()) place.changeRoadAdressName(dto.getRoadAddressName());
        if (place.getRegion1DepthName() != dto.getRegion1DepthName()) place.changeRegion1(dto.getRegion1DepthName());
        if (place.getRegion2DepthName() != dto.getRegion2DepthName()) place.changeRegion2(dto.getRegion2DepthName());
        if (place.getRegion3DepthName() != dto.getRegion3DepthName()) place.changeRegion3(dto.getRegion3DepthName());
        if (place.getLatitude() != dto.getLatitude()) place.changeLatitude(dto.getLatitude());
        if (place.getLongitude() != dto.getLongitude()) place.changeLongitude(dto.getLongitude());
        if (place.getImageUrl() != dto.getImageUrl()) place.changeImageUrl(dto.getImageUrl());
        if (place.getDescription() != dto.getDescription()) place.changeDescription(dto.getDescription());

        return place.getId();

    }

    public void deletePlace(Long id) {

        placeAdminRepository.deleteById(id);
    }
}
