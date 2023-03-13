package com.dateplanner.admin.place.repository;

import com.dateplanner.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceAdminRepository extends JpaRepository<Place, Long> {

    List<Place> findByImageUrlIsNull();

    List<Place> findByImageUrlIs(String url);

    @Query("select p.placeId from Place p where p.imageUrl is null")
    List<String> findPlaceIdByImageUrlIsNull();

    @Modifying(clearAutomatically = true)
    @Query("update Place p set p.imageUrl = :imageUrl, p.description = :description where p.placeId = :placeId")
    void updateImageUrlAndDescription(@Param("placeId") String placeId,
                                      @Param("imageUrl") String imageUrl,
                                      @Param("description") String description);

    void deleteById(Long id);

}
