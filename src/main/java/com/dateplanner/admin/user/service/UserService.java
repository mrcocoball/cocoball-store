package com.dateplanner.admin.user.service;

import com.dateplanner.admin.user.dto.UserModifyRequestDto;
import com.dateplanner.admin.user.dto.UserRequestDto;
import com.dateplanner.advice.exception.UserNotFoundApiException;
import com.dateplanner.admin.user.dto.UserResponseDto;
import com.dateplanner.admin.user.entity.User;
import com.dateplanner.admin.user.repository.UserRepository;
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
        return userRepository.findByEmail(dto.getEmail()).get().getEmail();
    }

    @Transactional(readOnly = true)
    public UserResponseDto findByNickname(String nickname) {
        User user = userRepository.findByNickname(nickname).orElseThrow(UserNotFoundApiException::new);
        return new UserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundApiException::new);
        return new UserResponseDto(user);
    }

    public String update(String email, UserModifyRequestDto dto) {
        User updateUser = userRepository.findByEmail(email).orElseThrow(UserNotFoundApiException::new);

        if (dto.getPassword() != null) { updateUser.changePassword(dto.getPassword()); }
        if (dto.getEmail() != null) { updateUser.changeEmail(dto.getEmail()); }
        if(dto.getNickname() != null) { updateUser.changeNickname(dto.getNickname()); }

        return email;
    }

    public void delete(String email) {
        User targetUser = userRepository.findByEmail(email).orElseThrow(UserNotFoundApiException::new);
        targetUser.changeDelete(true);
    }
}
