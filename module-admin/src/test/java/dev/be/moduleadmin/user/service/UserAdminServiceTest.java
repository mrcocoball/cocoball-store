package dev.be.moduleadmin.user.service;

import dev.be.fixture.Fixture;
import dev.be.moduleadmin.advice.exception.UserExistException;
import dev.be.moduleadmin.advice.exception.UserNicknameDuplicateException;
import dev.be.moduleadmin.advice.exception.UserNotFoundApiException;
import dev.be.moduleadmin.user.dto.UserModifyRequestDto;
import dev.be.moduleadmin.user.dto.UserRequestDto;
import dev.be.moduleadmin.user.dto.UserResponseDto;
import dev.be.moduleadmin.user.repository.UserAdminCustomRepository;
import dev.be.modulecore.domain.user.User;
import dev.be.modulecore.repositories.support.UserAdminRepository;
import dev.be.modulecore.repositories.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[단일] 회원 관리 서비스 - 회원 조회 / 저장 / 수정 / 삭제 테스트")
@ExtendWith(MockitoExtension.class)
class UserAdminServiceTest {

    @InjectMocks
    private UserAdminService sut;

    @Mock
    private UserAdminRepository userAdminRepository;

    @Mock
    private UserAdminCustomRepository userAdminCustomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @DisplayName("READ - 회원 리스트 조회")
    @Test
    public void 회원_리스트_조회() {

        // Given
        String email = "test";
        String nickname = "test";
        boolean deleted = false;
        boolean social = false;
        LocalDate startDate = LocalDate.now();
        LocalDate targetDate = LocalDate.now();
        Pageable pageable = Pageable.ofSize(10);
        given(userAdminCustomRepository.userList(email, nickname, deleted, social, null, startDate, targetDate)).willReturn(Collections.emptyList());

        // When
        List<UserResponseDto> result = sut.getUserList(email, nickname, deleted, social, null, startDate, targetDate, pageable);

        // Then
        assertThat(result).isEmpty();
        then(userAdminCustomRepository).should().userList(email, nickname, deleted, social, null, startDate, targetDate);

    }

    @DisplayName("READ - 삭제된 회원 리스트 조회")
    @Test
    public void 삭제된_회원_리스트_조회() {

        // Given
        String email = "test";
        String nickname = "test";
        LocalDate startDate = LocalDate.now();
        LocalDate targetDate = LocalDate.now();
        Pageable pageable = Pageable.ofSize(10);
        given(userAdminCustomRepository.deletedUserList(email, nickname, startDate, targetDate)).willReturn(Collections.emptyList());

        // When
        List<UserResponseDto> result = sut.getDeletedUserList(email, nickname, startDate, targetDate, pageable);

        // Then
        assertThat(result).isEmpty();
        then(userAdminCustomRepository).should().deletedUserList(email, nickname, startDate, targetDate);

    }

    @DisplayName("READ - 회원 단건 조회")
    @Test
    public void 회원_단건_조회_성공() {

        // Given
        Long id = 1L;
        User user = Fixture.user();
        given(userAdminRepository.findByUid(id)).willReturn(Optional.of(user));

        // When
        UserResponseDto dto = sut.getUserByUid(id);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("email", user.getEmail())
                .hasFieldOrPropertyWithValue("nickname", user.getNickname());
        then(userAdminRepository).should().findByUid(id);

    }

    @DisplayName("READ - 회원 단건 조회 - 실패(존재하지 않는 회원)")
    @Test
    public void 회원_단건_조회_실패() {

        // Given
        Long id = 0L;
        given(userAdminRepository.findByUid(id)).willReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundApiException.class, () -> {
            sut.getUserByUid(id);
        });
        then(userAdminRepository).should().findByUid(id);

    }

    @DisplayName("CREATE - 회원 저장")
    @Test
    public void 회원_저장_성공() {

        // Given
        User user = Fixture.user();
        UserRequestDto dto = Fixture.userRequestDto();
        given(userRepository.findByNickname(dto.getNickname())).willReturn(Optional.empty());
        given(userRepository.findByEmail(dto.getEmail())).willReturn(Optional.empty());
        given(userAdminRepository.save(any(User.class))).willReturn(user);

        // When
        sut.saveUser(dto);

        // Then
        then(userRepository).should().findByNickname(dto.getNickname());
        then(userRepository).should().findByEmail(dto.getEmail());
        then(userAdminRepository).should().save(any(User.class));
    }

    @DisplayName("CREATE - 회원 저장 - 실패(닉네임 중복)")
    @Test
    public void 회원_저장_실패_닉네임_중복() {

        // Given
        UserRequestDto dto = Fixture.userRequestDto();
        given(userRepository.findByNickname(dto.getNickname())).willReturn(Optional.of(Fixture.user()));

        // When & Then
        assertThrows(UserNicknameDuplicateException.class, () -> {
            sut.saveUser(dto);
        });
        then(userRepository).should().findByNickname(dto.getNickname());
    }

    @DisplayName("CREATE - 회원 저장 - 실패(이메일 중복)")
    @Test
    public void 회원_저장_실패_이메일_중복() {

        // Given
        UserRequestDto dto = Fixture.userRequestDto();
        given(userRepository.findByNickname(dto.getNickname())).willReturn(Optional.empty());
        given(userRepository.findByEmail(dto.getEmail())).willReturn(Optional.of(Fixture.user()));

        // When & Then
        assertThrows(UserExistException.class, () -> {
            sut.saveUser(dto);
        });
        then(userRepository).should().findByNickname(dto.getNickname());
        then(userRepository).should().findByEmail(dto.getEmail());
    }

    @DisplayName("UPDATE - 회원 수정")
    @Test
    public void 회원_수정_성공() {

        // Given
        User user = Fixture.user();
        UserModifyRequestDto updateRequestDto = Fixture.userModifyRequestDto();
        given(userAdminRepository.findByUid(updateRequestDto.getUid())).willReturn(Optional.of(user));

        // When
        sut.updateUser(updateRequestDto);

        // Then
        assertThat(user).hasFieldOrPropertyWithValue("nickname", updateRequestDto.getNickname());
        then(userAdminRepository).should().findByUid(updateRequestDto.getUid());
    }

    @DisplayName("UPDATE - 회원 수정 - 실패(존재하지 않는 회원)")
    @Test
    public void 회원_수정_실패() {

        // Given
        UserModifyRequestDto updateRequestDto = Fixture.userModifyRequestDto();
        given(userAdminRepository.findByUid(updateRequestDto.getUid())).willReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundApiException.class, () -> {
            sut.updateUser(updateRequestDto);
        });
        then(userAdminRepository).should().findByUid(updateRequestDto.getUid());
    }

    @DisplayName("DELETE(UPDATE) - 회원 삭제")
    @Test
    public void 회원_삭제_성공() {

        // Given
        Long id = 1L;
        User user = Fixture.user();
        given(userAdminRepository.findByUid(id)).willReturn(Optional.of(user));

        // When
        sut.deleteUser(id);

        // Then
        assertThat(user).hasFieldOrPropertyWithValue("deleted", true);
        then(userAdminRepository).should().findByUid(id);
    }

    @DisplayName("DELETE(UPDATE) - 회원 삭제 - 실패(존재하지 않는 회원)")
    @Test
    public void 회원_삭제_실패() {

        // Given
        Long id = 1L;
        given(userAdminRepository.findByUid(id)).willReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundApiException.class, () -> {
            sut.deleteUser(id);
        });
        then(userAdminRepository).should().findByUid(id);
    }


}