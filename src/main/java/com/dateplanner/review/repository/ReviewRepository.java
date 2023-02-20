package com.dateplanner.review.repository;

import com.dateplanner.review.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"user", "place", "images"})
    List<Review> findByKpid(String placeId);

    @EntityGraph(attributePaths = {"user", "place", "images"})
    List<Review> findByUser_Nickname(String nickname);

    @Override
    @EntityGraph(attributePaths = {"user", "place", "images"})
    Optional<Review> findById(Long id);
}
