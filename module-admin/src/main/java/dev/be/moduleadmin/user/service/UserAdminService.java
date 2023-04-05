package dev.be.moduleadmin.user.service;

import dev.be.moduleadmin.advice.exception.UserNotFoundApiException;
import dev.be.moduleadmin.user.dto.UserModifyRequestDto;
import dev.be.moduleadmin.user.dto.UserPasswordRequestDto;
import dev.be.moduleadmin.user.dto.UserRequestDto;
import dev.be.moduleadmin.user.dto.UserResponseDto;
import dev.be.moduleadmin.user.repository.UserAdminCustomRepository;
import dev.be.modulecore.domain.support.UserPasswordRequest;
import dev.be.modulecore.domain.user.User;
import dev.be.modulecore.repositories.support.UserAdminRepository;
import dev.be.modulecore.repositories.support.UserPasswordRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Comparator;
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
                                             LocalDate targetDate,
                                             Pageable pageable) {

        List<UserResponseDto> dtos = userAdminCustomRepository.userList(email, nickname, deleted, social, provider, startDate, targetDate).stream().collect(Collectors.toList());

        if (pageable.getSort().equals(Sort.by(Sort.Direction.DESC, "modifiedAt"))) dtos.sort(Comparator.comparing(UserResponseDto::getModifiedAt).reversed());
        if (pageable.getSort().equals(Sort.by(Sort.Direction.ASC, "modifiedAt"))) dtos.sort(Comparator.comparing(UserResponseDto::getModifiedAt));

        return dtos;

    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getDeletedUserList(String email,
                                                    String nickname,
                                                    LocalDate startDate,
                                                    LocalDate targetDate,
                                                    Pageable pageable) {

        List<UserResponseDto> dtos = userAdminCustomRepository.deletedUserList(email, nickname, startDate, targetDate).stream().collect(Collectors.toList());

        if (pageable.getSort().equals(Sort.by(Sort.Direction.DESC, "modifiedAt"))) dtos.sort(Comparator.comparing(UserResponseDto::getModifiedAt).reversed());
        if (pageable.getSort().equals(Sort.by(Sort.Direction.ASC, "modifiedAt"))) dtos.sort(Comparator.comparing(UserResponseDto::getModifiedAt));

        return dtos;

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
