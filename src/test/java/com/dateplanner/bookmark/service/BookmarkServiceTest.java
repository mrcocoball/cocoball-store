package com.dateplanner.bookmark.service;

import com.dateplanner.bookmark.entity.Bookmark;
import com.dateplanner.bookmark.repository.BookmarkRepository;
import com.dateplanner.fixture.Fixture;
import com.dateplanner.place.entity.Place;
import com.dateplanner.place.repository.PlaceRepository;
import com.dateplanner.admin.user.entity.User;
import com.dateplanner.admin.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("[단일] 북마크 서비스 - 북마크 저장 테스트")
@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @InjectMocks private BookmarkService sut;

    @Mock private BookmarkRepository bookmarkRepository;

    @Mock private PlaceRepository placeRepository;

    @Mock private UserRepository userRepository;


    @DisplayName("CREATE - 북마크 저장")
    @Test
    public void 북마크_저장_성공() {

        // Given
        String uid = "test";
        String kpid = "test";
        User user = Fixture.user();
        Place place = Fixture.place();
        given(userRepository.findByUid(uid)).willReturn(Optional.of(user));
        given(placeRepository.findByPlaceId(kpid)).willReturn(Optional.of(place));
        given(bookmarkRepository.save(any(Bookmark.class))).willReturn(Fixture.bookmark());

        // When
        sut.saveBookmark(uid, kpid);

        // Then
        then(userRepository).should().findByUid(uid);
        then(placeRepository).should().findByPlaceId(kpid);
        then(bookmarkRepository).should().save(any(Bookmark.class));

    }

    @DisplayName("DELETE - 북마크 삭제")
    @Test
    public void 북마크_삭제_성공() {

        // Given
        Long id = 1L;
        willDoNothing().given(bookmarkRepository).deleteById(id);

        // When
        sut.deleteBookmark(id);

        // Then
        then(bookmarkRepository).should().deleteById(id);

    }

}