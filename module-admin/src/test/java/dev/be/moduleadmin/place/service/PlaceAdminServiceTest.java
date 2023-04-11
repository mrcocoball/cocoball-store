package dev.be.moduleadmin.place.service;

import dev.be.fixture.Fixture;
import dev.be.moduleadmin.advice.exception.PlaceNotFoundApiException;
import dev.be.moduleadmin.kakao.service.KakaoAddressSearchService;
import dev.be.moduleadmin.place.dto.PlaceAdminDetailDto;
import dev.be.moduleadmin.place.dto.PlaceModifyRequestDto;
import dev.be.moduleadmin.place.dto.PlaceStatusDto;
import dev.be.moduleadmin.place.repository.PlaceAdminCustomRepository;
import dev.be.modulecore.domain.place.Place;
import dev.be.modulecore.repositories.support.PlaceAdminRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@DisplayName("[단일] 장소 관리 서비스 - 장소 조회 / 수정 / 삭제 테스트")
@ExtendWith(MockitoExtension.class)
class PlaceAdminServiceTest {

    @InjectMocks
    private PlaceAdminService sut;

    @Mock
    private PlaceAdminRepository placeAdminRepository;

    @Mock
    private PlaceAdminCustomRepository placeAdminCustomRepository;


    @DisplayName("READ - 장소 리스트 조회")
    @Test
    public void 장소_리스트_조회() {

        // Given
        String region1 = "test1";
        String region2 = "test2";
        String region3 = "test3";
        String categoryId = "test";
        Long id = 1L;
        String placeId = "1";
        String placeName = "placeName";
        Long reviewCount = 0L;
        LocalDate startDate = LocalDate.now();
        LocalDate targetDate = LocalDate.now();
        given(placeAdminCustomRepository.placeList(region1, region2, region3, categoryId, id, placeId, placeName, reviewCount, startDate, targetDate))
                .willReturn(Collections.emptyList());

        // When
        List<PlaceAdminDetailDto> result = sut.getPlaceList(region1, region2, region3, categoryId, id, placeId, placeName, reviewCount, startDate, targetDate);

        // Then
        assertThat(result).isEmpty();
        then(placeAdminCustomRepository.placeList(region1, region2, region3, categoryId, id, placeId, placeName, reviewCount, startDate, targetDate));

    }

    @DisplayName("READ - 장소 단건 조회")
    @Test
    public void 장소_단건_조회_성공() {

        // Given
        Long id = 1L;
        Place place = Fixture.place();
        given(placeAdminRepository.findById(id)).willReturn(Optional.of(place));

        // When
        PlaceAdminDetailDto dto = sut.getPlace(id);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("id", place.getId())
                .hasFieldOrPropertyWithValue("placeId", place.getPlaceId());
        then(placeAdminRepository.findById(id));

    }

    @DisplayName("READ - 장소 단건 조회 - 실패(존재하지 않는 장소)")
    @Test
    public void 장소_단건_조회_실패() {

        // Given
        Long id = 0L;
        given(placeAdminRepository.findById(id)).willReturn(Optional.empty());

        // When & Then
        assertThrows(PlaceNotFoundApiException.class, () -> {
            sut.getPlace(id);
        });
        then(placeAdminRepository.findById(id));

    }

    @DisplayName("UPDATE - 장소 수정")
    @Test
    public void 장소_수정_성공() {

        // Given
        Place place = Fixture.place();
        PlaceModifyRequestDto dto = Fixture.placeModifyRequestDto();
        given(placeAdminRepository.findById(dto.getId())).willReturn(Optional.of(place));

        // When
        sut.updatePlace(dto);

        // Then
        assertThat(place)
                .hasFieldOrPropertyWithValue("placeName", dto.getPlaceName())
                .hasFieldOrPropertyWithValue("placeId", dto.getPlaceId())
                .hasFieldOrPropertyWithValue("placeUrl", dto.getPlaceUrl());
        then(placeAdminRepository.findById(dto.getId()));

    }

    @DisplayName("UPDATE - 장소 수정 - 실패(존재하지 않는 장소)")
    @Test
    public void 장소_수정_실패() {

        // Given
        PlaceModifyRequestDto dto = Fixture.placeModifyRequestDto();
        given(placeAdminRepository.findById(dto.getId())).willReturn(Optional.empty());

        // When & Then
        assertThrows(PlaceNotFoundApiException.class, () -> {
            sut.updatePlace(dto);
        });
        then(placeAdminRepository.findById(dto.getId()));

    }

    @DisplayName("DELETE - 장소 삭제")
    @Test
    public void 장소_삭제_성공() {

        // Given
        Long id = 1L;
        willDoNothing().given(placeAdminRepository).deleteById(id);

        // When
        sut.deletePlace(id);

        // Then
        then(placeAdminRepository).should().deleteById(id);

    }

    @DisplayName("READ - 크롤링이 필요한 장소 리스트 조회")
    @Test
    public void 크롤링이_필요한_장소_리스트_조회() {

        // Given
        given(placeAdminRepository.findPlaceIdByImageUrlIsNull()).willReturn(Collections.emptyList());

        // When
        List<PlaceStatusDto> result = sut.getImageUrlNullPlacesV1();

        // Then
        assertThat(result).isEmpty();
        then(placeAdminRepository.findPlaceIdByImageUrlIsNull());

    }

    @DisplayName("READ - 크롤링했으나 이미지가 존재하지 않는 장소 리스트 조회")
    @Test
    public void 크롤링했으나_이미지가_존재하지_않는_장소_리스트_조회() {

        // Given
        String NOT_EXIST_IMAGE = "NOT EXISTS";
        given(placeAdminRepository.findByImageUrlIs(NOT_EXIST_IMAGE)).willReturn(Collections.emptyList());

        // When
        List<PlaceStatusDto> result = sut.getImageUrlNotExistPlacesV1();

        // Then
        assertThat(result).isEmpty();
        then(placeAdminRepository.findByImageUrlIs(NOT_EXIST_IMAGE));

    }

}