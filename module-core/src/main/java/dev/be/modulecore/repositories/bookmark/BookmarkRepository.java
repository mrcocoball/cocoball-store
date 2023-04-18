package dev.be.modulecore.repositories.bookmark;

import dev.be.modulecore.domain.bookmark.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    /* 테스트 수정 후 삭제 필요 */
    @EntityGraph(attributePaths = {"user", "place"})
    List<Bookmark> findByUser_Email(String email);

    @EntityGraph(attributePaths = {"user", "place"})
    Page<Bookmark> findByUser_Email(String email, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "place"})
    void deleteById(Long id);

    @EntityGraph(attributePaths = {"user", "place"})
    void deleteByPlace_PlaceId(String placeId);

    @EntityGraph(attributePaths = {"user", "place"})
    @Query("select b" +
            " from Bookmark b" +
            " join fetch b.user u" +
            " where b.kpid = :placeId and u.nickname = :nickname")
    List<Bookmark> isBookmarked(@Param("placeId") String placeId, @Param("nickname") String nickname);

}
