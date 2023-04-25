package dev.be.modulecore.repositories.review;

import dev.be.modulecore.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"user", "place"})
    Page<Review> findByKpid(String placeId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "place"})
    Page<Review> findByUser_Nickname(String nickname, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"user", "place"})
    Optional<Review> findById(Long id);
}
