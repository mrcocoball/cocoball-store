package com.dateplanner.place.repository;

import com.dateplanner.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    boolean existsByPlaceId(String placeId);

    Optional<Place> findByPlaceId(String placeId);

    List<Place> findByAddressNameStartingWith(String address);
}
