package com.dateplanner.place.service;

import com.dateplanner.advice.exception.PlaceNotFoundApiException;
import com.dateplanner.api.PaginationService;
import com.dateplanner.bookmark.service.BookmarkService;
import com.dateplanner.kakao.dto.DocumentDto;
import com.dateplanner.kakao.dto.KakaoApiResponseDto;
import com.dateplanner.place.dto.PlaceDetailDto;
import com.dateplanner.place.dto.PlaceDto;
import com.dateplanner.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlaceApiService {

    private final PlaceRepository placeRepository;
    private final BookmarkService bookmarkService;
    private final PaginationService paginationService;

    private static final int MAX_LADIUS = 5; // km


    /**
     * 카카오 주소 검색 API + 카테고리 장소 검색 결과를 그대로 화면으로 처리
     */
    public Page<DocumentDto> getPlacesByKakao(KakaoApiResponseDto dto, Pageable pageable) {

        List<DocumentDto> dtos = dto.getDocumentList();

        return paginationService.listToPage(dtos, pageable);

    }

    /**
     * 카카오 주소 검색 API으로 반환한 좌표 + 주소 기준 DB 조회 후 거리 계산하여 장소 출력 후 화면으로 처리
     * @param addressDto 주소 검색 API로 전달 받은 DocumentDto
     * @param dto 카테고리 검색 API로 전달 받은 KakaoApiResponseDto
     */


    public Page<PlaceDto> getPlaces(DocumentDto addressDto, KakaoApiResponseDto dto, List<String> region2List, List<String> categories, Pageable pageable) {

        // 거리가 가장 가까운 장소 가져오기 + 해당 장소의 1depth_name 가져오기
        DocumentDto documentDto = dto.getDocumentList().get(0);
        String region1 = documentDto.getRegion1DepthName();

        log.info("[PlaceApiService getPlaces] target address : {}, {}", region1, region2List);

        // 인덱스 계산용 시간 측정
        long beforeTime = System.currentTimeMillis();

        // 1depth_name, 2depth_name 기준으로 장소 Dto 가져오기
        List<PlaceDto> placeDtos = placeRepository.findByRegion1DepthNameAndRegion2DepthNameAndCategory(region1, region2List, categories)
                .stream()
                .map(place -> PlaceDto.from(place, false))
                .collect(Collectors.toList());

        log.info("[PlaceApiService getPlaces] places {} found", placeDtos.size());

        // 인덱스 계산용 시간 측정
        long afterTime = System.currentTimeMillis();
        log.info("elapsed time : " + (afterTime-beforeTime));

        // 장소 Dto 와 기준점과의 거리 계산하여 필터링 후 최종 리스트에 저장
        double latitude = addressDto.getLatitude();
        double longitude = addressDto.getLongitude();
        log.info("[PlaceApiService getPlaces] target latitude {}, target longitude {}", latitude, longitude);

        List<PlaceDto> result = new ArrayList<>();

        for (PlaceDto placeDto : placeDtos) {
            double distance = calculateDistance(latitude, longitude,
                                placeDto.getLatitude(), placeDto.getLongitude()) * 0.001;
            log.info("id : {}, distance : {}", placeDto.getId(), distance);

            if(distance <= MAX_LADIUS) {
                placeDto.setDistance(distance * 1000);
                result.add(placeDto);
            }
        }

        log.info("[PlaceApiService getPlaces] finally places {} found", result.size());

        // 페이징 처리
        return paginationService.listToPage(result, pageable);
    }

    /**
     * KAKAO API 상의 place_id를 DB에서 조회하여 정보를 가져온다 (상세 정보, 리뷰 유무, 평점, 리뷰 수 등)
     */
    public PlaceDetailDto getPlace(String placeId, String uid) {

        boolean isBookmarked = bookmarkService.isExist(placeId, uid);

        return placeRepository.findByPlaceId(placeId).map(place -> PlaceDetailDto.from(place, isBookmarked)).orElseThrow(PlaceNotFoundApiException::new);
    }


    /**
     * 두 지점의 좌표를 이용한 거리 계산 메서드
     */
    // Haversine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371; // Kilometers
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }

}
