package com.dateplanner.admin.user.service;

import com.dateplanner.admin.user.dto.UserModifyRequestDto;
import com.dateplanner.admin.user.dto.UserRequestDto;
import com.dateplanner.admin.user.repository.UserAdminRepository;
import com.dateplanner.advice.exception.UserNotFoundApiException;
import com.dateplanner.admin.user.dto.UserResponseDto;
import com.dateplanner.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Service
@Transactional
public class UserAdminService {

    private final UserAdminRepository userAdminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUserList() {

        return userAdminRepository.findAll().stream().map(UserResponseDto::from).collect(Collectors.toList());

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

        if (dto.getNickname() != null) { updateUser.changeNickname(dto.getNickname()); }

        return updateUser.getUid();
    }

    public void deleteUser(Long uid) {
        User targetUser = userAdminRepository.findByUid(uid).orElseThrow(UserNotFoundApiException::new);
        targetUser.changeDelete(true);
    }
}
