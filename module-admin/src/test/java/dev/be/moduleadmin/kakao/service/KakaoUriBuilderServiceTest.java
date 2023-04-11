package dev.be.moduleadmin.kakao.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[단일] 카카오 API - URI 빌더 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class KakaoUriBuilderServiceTest {

    @InjectMocks
    private KakaoUriBuilderService sut;

    @DisplayName("주소 검색용 URI 빌드 테스트_성공")
    @Test
    public void 주소_검색용_URI_빌드_정상처리() {

        // Given
        String address = "서울 성북구";
        Charset charset = StandardCharsets.UTF_8;

        // When
        URI uri = sut.buildUriForAddressSearch(address);
        String decodedResult = URLDecoder.decode(uri.toString(), charset);

        // Then
        assertThat(decodedResult).isEqualTo("https://dapi.kakao.com/v2/local/search/address.json?query=서울 성북구");
    }

    @DisplayName("카테고리 장소 검색용 URI 빌드 테스트_성공")
    @Test
    public void 카테고리_장소_검색용_URI_빌드_정상처리() {

        // Given
        String category = "CE7";
        double y = 37.514322572335935;
        double x = 127.06283102249932;
        int radius = 2;
        int page = 1;
        Charset charset = StandardCharsets.UTF_8;

        // When
        URI uri = sut.buildUriForCategorySearch(y, x, radius, category, page);
        String decodedResult = URLDecoder.decode(uri.toString(), charset);

        // Then
        assertThat(decodedResult).isEqualTo("https://dapi.kakao.com/v2/local/search/category.json?category_group_code=CE7&y=37.514322572335935&x=127.06283102249932&radius=2000&sort=distance&page=1");
    }

}