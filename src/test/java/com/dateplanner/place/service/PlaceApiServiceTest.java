package com.dateplanner.place.service;

import com.dateplanner.fixture.Fixture;
import com.dateplanner.kakao.dto.DocumentDto;
import com.dateplanner.kakao.dto.KakaoApiResponseDto;
import com.dateplanner.place.dto.PlaceDetailDto;
import com.dateplanner.place.dto.PlaceDto;
import com.dateplanner.place.entity.Place;
import com.dateplanner.place.repository.PlaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[통합] 장소 화면 처리 서비스 - 장소 조회 테스트")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PlaceApiServiceTest {

    @Autowired
    private PlaceApiService sut;

    @MockBean
    private PlaceRepository placeRepository;


    @DisplayName("READ - 장소 리스트 조회")
    @Test
    public void 장소_리스트_조회_성공() {

        // Given
        KakaoApiResponseDto testKakaoApiResponseDto = new KakaoApiResponseDto();
        DocumentDto testDocumentDto = Fixture.documentDto();
        List<DocumentDto> testDocumentList = new ArrayList<>();
        testDocumentList.add(testDocumentDto);
        testKakaoApiResponseDto.setDocumentList(testDocumentList);
        String testRegion1 = testDocumentDto.getRegion1DepthName();

        List<String> testRegion2List = new ArrayList<>();
        String testRegion2 = "관악구";
        testRegion2List.add(testRegion2);

        List<String> testCategoryList = new ArrayList<>();
        String testCategory = "CE7";
        testCategoryList.add(testCategory);

        Pageable pageable = Pageable.ofSize(10);
        given(placeRepository.findByRegion1DepthNameAndRegion2DepthNameAndCategory(testRegion1, testRegion2List, testCategoryList)).willReturn(Collections.emptyList());

        // When
        Page<PlaceDto> result = sut.getPlaces(testDocumentDto, testKakaoApiResponseDto, testRegion2List, testCategoryList, pageable);

        // Then
        assertThat(result).isEmpty();
        then(placeRepository).should().findByRegion1DepthNameAndRegion2DepthNameAndCategory(testRegion1, testRegion2List, testCategoryList);

    }

    @DisplayName("READ - 장소 단건 조회")
    @Test
    public void 장소_단건_조회_성공() {

        // Given
        String uid = "test";
        String placeId = "1";
        Place place = Fixture.place();
        given(placeRepository.findByPlaceId(placeId)).willReturn(Optional.of(place));

        // When
        PlaceDetailDto dto = sut.getPlace(placeId, uid);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("id", place.getId())
                .hasFieldOrPropertyWithValue("placeId", place.getPlaceId());
        then(placeRepository).should().findByPlaceId(placeId);

    }


}