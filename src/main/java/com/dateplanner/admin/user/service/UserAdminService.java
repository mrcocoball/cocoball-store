package com.dateplanner.admin.user.service;

import com.dateplanner.admin.user.dto.UserModifyRequestDto;
import com.dateplanner.admin.user.dto.UserPasswordRequestDto;
import com.dateplanner.admin.user.dto.UserRequestDto;
import com.dateplanner.admin.user.dto.UserResponseDto;
import com.dateplanner.admin.user.entity.UserPasswordRequest;
import com.dateplanner.admin.user.repository.UserAdminCustomRepository;
import com.dateplanner.admin.user.repository.UserAdminRepository;
import com.dateplanner.admin.user.repository.UserPasswordRequestRepository;
import com.dateplanner.advice.exception.UserNotFoundApiException;
import com.dateplanner.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Service
@Transactional
public class UserAdminService {

    private final UserAdminRepository userAdminRepository;
    private final UserAdminCustomRepository userAdminCustomRepository;
    private final UserPasswordRequestRepository userPasswordRequestRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    public List<UserResponseDto> getUserList(String email,
                                             String nickname,
                                             boolean deleted,
                                             boolean social,
                                             String provider,
                                             LocalDate startDate,
                                             LocalDate targetDate) {

        log.info("[UserAdminService getUserListByDQ] search condition : {}, {}, {}, {}, {}", deleted, social, provider, startDate, targetDate);

        return userAdminCustomRepository.userList(email, nickname, deleted, social, provider, startDate, targetDate).stream().collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getDeletedUserList(String email,
                                                    String nickname,
                                                    LocalDate startDate,
                                                    LocalDate targetDate) {

        return userAdminCustomRepository.deletedUserList(email, nickname,startDate, targetDate).stream().collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserByUid(Long uid) {

        return userAdminRepository.findByUid(uid).map(UserResponseDto::from).orElseThrow(UserNotFoundApiException::new);

    }

    public Long saveUser(UserRequestDto dto) {
        User user = userAdminRepository.save(dto.toEntity(passwordEncoder));
        return user.getUid();
    }

    public Long updateUser(UserModifyRequestDto dto) {
        User updateUser = userAdminRepository.findByUid(dto.getUid()).orElseThrow(UserNotFoundApiException::new);

        if (dto.getNickname() != null) {
            updateUser.changeNickname(dto.getNickname());
        }

        return updateUser.getUid();
    }

    public void deleteUser(Long uid) {
        User targetUser = userAdminRepository.findByUid(uid).orElseThrow(UserNotFoundApiException::new);
        targetUser.changeDelete(true);
    }


    /**
     * 비밀번호 변경 요청 관련
     */

    @Transactional(readOnly = true)
    public List<UserPasswordRequestDto> getUserPasswordRequestList() {

        return userPasswordRequestRepository.findAll().stream().map(UserPasswordRequestDto::from).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public UserPasswordRequestDto getUserPasswordRequest(Long id) {

        return userPasswordRequestRepository.findById(id).map(UserPasswordRequestDto::from).orElseThrow(EntityNotFoundException::new);

    }

    public void userPasswordRequestUpdate(Long id, Long uid) {

        UserPasswordRequest userPasswordRequest = userPasswordRequestRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        User user = userAdminRepository.findByUid(uid).orElseThrow(UserNotFoundApiException::new);
        userPasswordRequest.complete();

    }

    public void deleteUserPasswordRequest(Long id) {

        userPasswordRequestRepository.deleteById(id);

    }


}
