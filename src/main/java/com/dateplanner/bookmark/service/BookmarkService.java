package com.dateplanner.bookmark.service;

import com.dateplanner.advice.exception.BookmarkDuplicateException;
import com.dateplanner.advice.exception.PlaceNotFoundApiException;
import com.dateplanner.advice.exception.UserNotFoundApiException;
import com.dateplanner.bookmark.entity.Bookmark;
import com.dateplanner.bookmark.repository.BookmarkRepository;
import com.dateplanner.place.entity.Place;
import com.dateplanner.place.repository.PlaceRepository;
import com.dateplanner.user.entity.User;
import com.dateplanner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Service
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public Long saveBookmark(String nickname, String placeId) {

        // 중복 체크
        log.info("[BookmarkService saveBookmark] bookmark duplication checking");

        if (isExist(placeId, nickname)) { throw new BookmarkDuplicateException(); }

        log.info("[BookmarkService saveBookmark] bookmark duplication check complete");

        User user = userRepository.findByNickname(nickname).orElseThrow(UserNotFoundApiException::new);
        Place place = placeRepository.findByPlaceId(placeId).orElseThrow(PlaceNotFoundApiException::new);

        Bookmark bookmark = Bookmark.of(user, place, placeId);
        bookmarkRepository.save(bookmark);

        return bookmark.getId();
    }

    public void deleteBookmark(Long id) {
        bookmarkRepository.deleteById(id);
    }

    public boolean isExist(String placeId, String nickname) {
        log.info("[BookmarkService isExist] checking...");
        return bookmarkRepository.isBookmarked(placeId, nickname).size() >= 1;
    }

}
