package dev.be.moduleapi.place.service;

import dev.be.fixture.Fixture;
import dev.be.moduleapi.advice.exception.PlaceNotFoundApiException;
import dev.be.moduleapi.advice.exception.SearchResultNotFoundException;
import dev.be.moduleapi.kakao.dto.DocumentDto;
import dev.be.moduleapi.kakao.dto.KakaoApiResponseDto;
import dev.be.moduleapi.place.dto.PlaceDetailDto;
import dev.be.moduleapi.place.dto.PlaceDto;
import dev.be.modulecore.domain.place.Place;
import dev.be.modulecore.repositories.place.PlaceRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void 장소_리스트_조회() {

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
        String sortType = "score";
        testCategoryList.add(testCategory);

        Pageable pageable = Pageable.ofSize(10);
        given(placeRepository.findByRegion1DepthNameAndRegion2DepthNameAndCategory(testRegion1, testRegion2List, testCategoryList)).willReturn(Collections.emptyList());

        // When
        Page<PlaceDto> result = sut.getPlaces(testDocumentDto, testKakaoApiResponseDto, testRegion2List, testCategoryList, pageable, sortType);

        // Then
        assertThat(result).isEmpty();
        then(placeRepository).should().findByRegion1DepthNameAndRegion2DepthNameAndCategory(testRegion1, testRegion2List, testCategoryList);

    }

    @DisplayName("READ - 장소 단건 조회 - 성공")
    @Test
    public void 장소_단건_조회_성공() {

        // Given
        String nickname = "test";
        String placeId = "1";
        Place place = Fixture.place();
        given(placeRepository.findByPlaceId(placeId)).willReturn(Optional.of(place));

        // When
        PlaceDetailDto dto = sut.getPlace(placeId, nickname);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("id", place.getId())
                .hasFieldOrPropertyWithValue("placeId", place.getPlaceId());
        then(placeRepository).should().findByPlaceId(placeId);

    }

    @DisplayName("READ - 장소 단건 조회 - 실패(존재하지 않는 장소)")
    @Test
    public void 장소_단건_조회_실패() {

        // Given
        String nickname = "test";
        String placeId = "INVALID";
        given(placeRepository.findByPlaceId(placeId)).willReturn(Optional.empty());

        // When & Then
        assertThrows(PlaceNotFoundApiException.class, () -> {
            sut.getPlace(placeId, nickname);
        });
        then(placeRepository).should().findByPlaceId(placeId);

    }


}