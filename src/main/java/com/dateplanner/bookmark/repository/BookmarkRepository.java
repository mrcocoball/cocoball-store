package com.dateplanner.bookmark.repository;

import com.dateplanner.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @EntityGraph(attributePaths = {"user", "place"})
    List<Bookmark> findByUser_Email(String email);

    @EntityGraph(attributePaths = {"user", "place"})
    void deleteById(Long id);

    @EntityGraph(attributePaths = {"user", "place"})
    @Query("select b" +
            " from Bookmark b" +
            " join fetch b.user u" +
            " where b.kpid = :placeId and u.nickname = :nickname")
    List<Bookmark> isBookmarked(@Param("placeId") String placeId, @Param("nickname") String nickname);

}
