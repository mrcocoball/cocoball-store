package dev.be.moduleadmin.place.service;

import dev.be.moduleadmin.advice.exception.PlaceNotFoundApiException;
import dev.be.moduleadmin.kakao.dto.DocumentDto;
import dev.be.moduleadmin.kakao.service.KakaoAddressSearchService;
import dev.be.moduleadmin.place.dto.PlaceAdminDetailDto;
import dev.be.moduleadmin.place.dto.PlaceModifyRequestDto;
import dev.be.moduleadmin.place.dto.PlaceRequestDto;
import dev.be.moduleadmin.place.dto.PlaceStatusDto;
import dev.be.moduleadmin.place.repository.PlaceAdminCustomRepository;
import dev.be.modulecore.domain.place.Place;
import dev.be.modulecore.repositories.support.PlaceAdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Transactional
@Service
public class PlaceAdminService {

    private final PlaceAdminRepository placeAdminRepository;
    private final PlaceAdminCustomRepository placeAdminCustomRepository;
    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final static String NOT_EXIST_IMAGE = "NOT EXISTS";


    /**
     * 장소 CRUD 관련
     */

    @Transactional(readOnly = true)
    public List<PlaceAdminDetailDto> getPlaceList(String region1, String region2, String region3,
                                                  String categoryId, Long id, String placeId, String placeName,
                                                  Long reviewCount, LocalDate startDate, LocalDate targetDate) {

        return placeAdminCustomRepository.placeList(region1, region2, region3, categoryId, id, placeId, placeName, reviewCount, startDate, targetDate);

    }

    @Transactional(readOnly = true)
    public PlaceAdminDetailDto getPlace(Long id) {

        return placeAdminRepository.findById(id).map(PlaceAdminDetailDto::from).orElseThrow(PlaceNotFoundApiException::new);

    }

    public Long savePlace(PlaceRequestDto dto) {

        DocumentDto addressDto = kakaoAddressSearchService.requestAddressSearch(dto.getAddressName()).getDocumentList().get(0);

        Place place = placeAdminRepository.save(dto.toEntity(
                dto.getCategoryGroupId(), dto.getPlaceName(), dto.getPlaceId(), dto.getPlaceUrl(), dto.getAddressName(),
                dto.getRoadAddressName(), dto.getRegion2DepthName(), dto.getRegion2DepthName(), dto.getRegion3DepthName(),
                Double.valueOf(addressDto.getLongitude()), Double.valueOf(addressDto.getLatitude()), dto.getImageUrl(), dto.getDescription()
        ));

        return place.getId();

    }

    public Long updatePlace(PlaceModifyRequestDto dto) {

        Place place = placeAdminRepository.findById(dto.getId()).orElseThrow(PlaceNotFoundApiException::new);

        if (place.getPlaceId() != dto.getPlaceId()) place.changePlaceId(dto.getPlaceId());
        if (place.getPlaceName() != dto.getPlaceName()) place.changePlaceName(dto.getPlaceName());
        if (place.getPlaceUrl() != dto.getPlaceUrl()) place.changePlaceUrl(dto.getPlaceUrl());
        if (place.getAddressName() != dto.getAddressName()) place.changeAddressName(dto.getAddressName());
        if (place.getRoadAddressName() != dto.getRoadAddressName()) place.changeRoadAdressName(dto.getRoadAddressName());
        if (place.getRegion1DepthName() != dto.getRegion1DepthName()) place.changeRegion1(dto.getRegion1DepthName());
        if (place.getRegion2DepthName() != dto.getRegion2DepthName()) place.changeRegion2(dto.getRegion2DepthName());
        if (place.getRegion3DepthName() != dto.getRegion3DepthName()) place.changeRegion3(dto.getRegion3DepthName());
        if (place.getImageUrl() != dto.getImageUrl()) place.changeImageUrl(dto.getImageUrl());
        if (place.getDescription() != dto.getDescription()) place.changeDescription(dto.getDescription());

        return place.getId();

    }

    public void deletePlace(Long id) {

        placeAdminRepository.deleteById(id);
    }


    /**
     * 업데이트가 필요한 장소 찾기
     */

    @Transactional(readOnly = true)
    public List<PlaceStatusDto> getImageUrlNullPlacesV1() {

        return placeAdminRepository.findByImageUrlIsNull().stream()
                .map(PlaceStatusDto::from)
                .sorted(Comparator.comparing(PlaceStatusDto::getModifiedAt).reversed())
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<PlaceStatusDto> getImageUrlNotExistPlacesV1() {

        return placeAdminRepository.findByImageUrlIs(NOT_EXIST_IMAGE).stream()
                .map(PlaceStatusDto::from)
                .sorted(Comparator.comparing(PlaceStatusDto::getModifiedAt).reversed())
                .collect(Collectors.toList());
    }
}
