package com.dateplanner.place.service;

import com.dateplanner.api.dto.DocumentDto;
import com.dateplanner.place.dto.PlaceDto;
import com.dateplanner.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Service
public class PlaceApiService {

    private final PlaceRepository placeRepository;

    /**
     * 초기 버전은 KAKAO 카테고리 검색 결과를 받아 페이징 / 정렬 처리를 하고 화면 처리를 하지만
     * 추후 추가 기능이 붙어야 한다면 고도화가 필요할 것으로 보인다.
     */
    public Page<DocumentDto> places(List<DocumentDto> dtos, Pageable pageable) {
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), dtos.size());
        return new PageImpl<>(dtos.subList(start, end), pageable, dtos.size());
    }

    /**
     * KAKAO API 상의 place_id를 DB에서 조회하여 정보를 가져온다 (상세 정보, 리뷰 유무, 평점, 리뷰 수 등)
     */
    public PlaceDto getPlace(String placeId) {
        return placeRepository.findByPlaceId(placeId).map(PlaceDto::from).orElseThrow(EntityNotFoundException::new);
    }

}
