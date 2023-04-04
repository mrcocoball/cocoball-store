package dev.be.modulecore.repositories.review;

import dev.be.modulecore.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /* 테스트 수정 후 삭제 필요*/
    @EntityGraph(attributePaths = {"user", "place", "images"})
    List<Review> findByKpid(String placeId);

    @EntityGraph(attributePaths = {"user", "place", "images"})
    Page<Review> findByKpid(String placeId, Pageable pageable);

    /* 테스트 수정 후 삭제 필요*/
    @EntityGraph(attributePaths = {"user", "place", "images"})
    List<Review> findByUser_Nickname(String nickname);

    @EntityGraph(attributePaths = {"user", "place", "images"})
    Page<Review> findByUser_Nickname(String nickname, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"user", "place", "images"})
    Optional<Review> findById(Long id);
}
