package dev.be.moduleadmin.place.service;

import dev.be.fixture.Fixture;
import dev.be.moduleadmin.place.dto.PlaceCrawlingDto;
import dev.be.modulecore.repositories.support.PlaceAdminRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[통합] 장소 크롤링 서비스 - 장소 크롤링 테스트")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PlaceCrawlingServiceTest {

    @Autowired
    private PlaceCrawlingService sut;

    @MockBean
    private PlaceAdminRepository placeAdminRepository;


    @DisplayName("READ - 장소 크롤링 성공")
    @Test
    public void 장소_크롤링_성공() {

        // Given
        List<String> crawlingIds = Fixture.crawlingIds();
        given(placeAdminRepository.findPlaceIdByImageUrlIsNull()).willReturn(crawlingIds);

        // When
        List<PlaceCrawlingDto> result = sut.searchAndCrawlingV1();

        // Then
        assertThat(result).isNotEmpty();
        then(placeAdminRepository.findPlaceIdByImageUrlIsNull());

    }

    @DisplayName("UPDATE - 장소 정보 업데이트 성공")
    @Test
    public void 장소_정보_업데이트_성공() {

        // Given
        List<PlaceCrawlingDto> dtos = Fixture.placeCrawlingDtoList();

        // When
        Long count = sut.updatePlacesV1(dtos);

        // Then
        assertThat(count).isEqualTo(1L);

    }


}