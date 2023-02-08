package com.dateplanner.place.service;

import com.dateplanner.fixture.Fixture;
import com.dateplanner.kakao.dto.DocumentDto;
import com.dateplanner.kakao.dto.KakaoApiResponseDto;
import com.dateplanner.place.entity.Place;
import com.dateplanner.place.repository.PlaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[단일] 장소 서비스 - 장소 저장 테스트")
@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {

    @InjectMocks
    private PlaceService sut;

    @Mock
    private PlaceRepository placeRepository;


    @DisplayName("CREATE - 카카오 API에서 가져온 장소 정보 저장")
    @Test
    public void 검색된_장소_저장_성공() {

        // Given
        KakaoApiResponseDto testKakaoApiResponseDto = new KakaoApiResponseDto();
        DocumentDto testDocumentDto = Fixture.documentDto();
        List<DocumentDto> testDocumentList = new ArrayList<>();
        testDocumentList.add(testDocumentDto);
        testKakaoApiResponseDto.setDocumentList(testDocumentList);
        given(placeRepository.save(any(Place.class))).willReturn(Fixture.place());

        // When
        sut.placePersist(testKakaoApiResponseDto);

        // Then
        then(placeRepository).should().save(any(Place.class));

    }

}