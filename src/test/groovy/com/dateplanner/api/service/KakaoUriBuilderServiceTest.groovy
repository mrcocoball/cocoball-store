package com.dateplanner.api.service

import org.junit.jupiter.api.DisplayName
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class KakaoUriBuilderServiceTest extends Specification {

    private KakaoUriBuilderService kakaoUriBuilderService

    def setup() {
        kakaoUriBuilderService = new KakaoUriBuilderService()
    }

    def "buildUriForAddressSearch - 한글 파라미터의 경우 정상적으로 인코딩한다"() {
        given:
        String address = "서울 성북구"
        def charset = StandardCharsets.UTF_8

        when:
        def uri = kakaoUriBuilderService.buildUriForAddressSearch(address)
        def decodedResult = URLDecoder.decode(uri.toString(), charset)

        then:
        decodedResult == "https://dapi.kakao.com/v2/local/search/address.json?query=서울 성북구"
    }

    def "buildUriForCategorySearch - "() {
        given:
        String category = "PM9"
        double y = 37.514322572335935
        double x = 127.06283102249932
        int radius = 2
        def charset = StandardCharsets.UTF_8

        when:
        def uri = kakaoUriBuilderService.buildUriForCategorySearch(y, x, radius, category)
        def decodedResult = URLDecoder.decode(uri.toString(), charset)

        then:
        decodedResult == "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=PM9&y=37.514322572335935&x=127.06283102249932&radius=2000&sort=distance"
    }


}
