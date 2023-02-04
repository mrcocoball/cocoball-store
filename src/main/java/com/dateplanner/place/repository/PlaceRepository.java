package com.dateplanner.place.repository;

import com.dateplanner.place.entity.Place;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    boolean existsByPlaceId(String placeId);

    Optional<Place> findByPlaceId(String placeId);

    @Query("select p from Place p left join fetch p.category c " +
            "where p.region1DepthName = :region1 and p.region2DepthName in :region2s and c.id = :category")
    List<Place> findByRegion1DepthNameAndRegion2DepthNameAndCategory(@Param("region1") String region1,
                                                                     @Param("region2s") List<String> region2List,
                                                                     @Param("category") String category);

    @Query("select p.placeId from Place p where p.region1DepthName = :region1 and p.region2DepthName in :region2s")
    List<String> findPlaceIdByRegion1DepthNameAndRegion2DepthName(@Param("region1") String region1,
                                                                  @Param("region2s") List<String> region2list);
}
