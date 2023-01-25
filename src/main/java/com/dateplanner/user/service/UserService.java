package com.dateplanner.user.service;

import com.dateplanner.advice.exception.UserNotFoundApiException;
import com.dateplanner.user.dto.UserModifyRequestDto;
import com.dateplanner.user.dto.UserRequestDto;
import com.dateplanner.user.dto.UserResponseDto;
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
public class UserService {

    private final UserRepository userRepository;

    public String save(UserRequestDto dto) {
        userRepository.save(dto.toEntity());
        return userRepository.findByUid(dto.getUid()).get().getUid();
    }

    @Transactional(readOnly = true)
    public UserResponseDto findByUid(String uid) {
        User user = userRepository.findByUid(uid).orElseThrow(UserNotFoundApiException::new);
        return new UserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundApiException::new);
        return new UserResponseDto(user);
    }

    public String update(String uid, UserModifyRequestDto dto) {
        User updateUser = userRepository.findByUid(uid).orElseThrow(UserNotFoundApiException::new);

        if (dto.getPassword() != null) { updateUser.changePassword(dto.getPassword()); }
        if (dto.getEmail() != null) { updateUser.changeEmail(dto.getEmail()); }
        if(dto.getIntroduce() != null) { updateUser.changeIntroduce(dto.getIntroduce()); }

        return uid;
    }

    public void delete(String uid) {
        User targetUser = userRepository.findByUid(uid).orElseThrow(UserNotFoundApiException::new);
        targetUser.changeDelete(true);
    }
}
