package dev.be.moduleapi.place.service;

import dev.be.moduleapi.advice.exception.SearchResultNotFoundException;
import dev.be.moduleapi.place.repository.PlaceRecommendationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[통합] 장소 추천 서비스 - 장소 추천 테스트")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PlaceRecommendationServiceTest {

    @Autowired
    private PlaceRecommendationService sut;

    @MockBean
    private PlaceRecommendationRepository placeRecommendationRepository;


    @DisplayName("READ - 장소 추천 결과 조회")
    @Test
    public void 장소_추천_결과_조회_성공() {

        // Given
        String address1 = "서울";
        String address2 = "관악구";
        String address3 = "봉천동";

        Pageable pageable = Pageable.ofSize(10);
        given(placeRecommendationRepository.placeRecommendation(address1, address2, address3)).willReturn(Collections.emptyList());

        // When & Then
        assertThrows(SearchResultNotFoundException.class, () -> {
            sut.getPlaceRecommendation(address1, address2, address3, pageable);
        });
        then(placeRecommendationRepository).should().placeRecommendation(address1, address2, address3);

    }

}