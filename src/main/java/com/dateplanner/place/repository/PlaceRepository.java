package com.dateplanner.place.repository;

import com.dateplanner.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    boolean existsByPlaceId(String placeId);

    Optional<Place> findByPlaceId(String placeId);

    List<Place> findByAddressNameStartingWithAndCategory_Id(String address, String category);

    @Query("select p.placeId from Place p where p.region1DepthName = :region1 and p.region2DepthName = :region2")
    List<String> findPlaceIdByRegion1DepthNameAndRegion2DepthName(@Param("region1") String region1, @Param("region2") String region2);
}
