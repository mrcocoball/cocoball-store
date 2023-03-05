package com.dateplanner.user.controller;

import com.dateplanner.user.dto.UserLoginRequestDto;
import com.dateplanner.user.dto.UserSocialJoinRequestDto;
import com.dateplanner.user.dto.UserSocialLoginRequestDto;
import com.dateplanner.advice.ErrorCode;
import com.dateplanner.advice.exception.OAuthAgreementException;
import com.dateplanner.advice.exception.UserNotFoundApiException;
import com.dateplanner.api.model.CommonResult;
import com.dateplanner.api.model.SingleResult;
import com.dateplanner.api.service.ResponseService;
import com.dateplanner.security.dto.TokenDto;
import com.dateplanner.security.dto.TokenRequestDto;
import com.dateplanner.user.dto.UserJoinRequestDto;
import com.dateplanner.user.service.UserJoinService;
import com.dateplanner.security.oauth.dto.OAuthAccessTokenDto;
import com.dateplanner.security.oauth.dto.ProfileDto;
import com.dateplanner.security.oauth.service.OAuthProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j(topic = "CONTROLLER")
@Tag(name = "0. [로그인, 회원가입 화면] UserJoinApiController - 회원가입, 로그인, 토큰 발급 API")
@RequiredArgsConstructor
@RestController
public class UserJoinApiController {

    private final UserJoinService userJoinService;
    private final ResponseService responseService;
    private final OAuthProviderService oAuthProviderService;


    @Operation(summary = "[POST] 로그인 요청",
            description = "이메일, 비밀번호를 입력하여 로그인 요청을 합니다. <br>" +
                    "로그인이 완료되면 액세스 토큰, 리프레시 토큰이 발급이 됩니다. 본 API 문서에서 인증이 필요한 API를 테스트하기 위해서는 <br>" +
                    "본 로그인 요청을 통해 전달 받은 액세스 토큰을 사용해야 합니다.")
    @PostMapping("/api/v1/login")
    public SingleResult<TokenDto> login(
            @Parameter(description = "로그인 요청 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserLoginRequestDto.class))) @RequestBody UserLoginRequestDto dto) {

        TokenDto tokenDto = userJoinService.login(dto);

        return responseService.getSingleResult(tokenDto);

    }

    @Operation(summary = "[POST] 회원가입 요청",
            description = "이메일, 비밀번호, 닉네임을 입력하여 회원가입 요청을 합니다. <br>" +
                    "이메일, 닉네임 중복 체크가 필요하며 각 중복 체크용 API가 별도로 마련되어 있으니 확인 부탁드립니다.")
    @PostMapping("/api/v1/join")
    public SingleResult<String> join(
            @Parameter(description = "회원가입 요청 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserJoinRequestDto.class))) @Valid @RequestBody UserJoinRequestDto dto) {

        String joinedUserId = userJoinService.join(dto);

        return responseService.getSingleResult(joinedUserId);

    }

    @Operation(summary = "[POST] 액세스, 리프레시 토큰 재발급 요청",
            description = "액세스 토큰 만료 시 서버에 액세스, 리프레시 토큰 재발급을 요청합니다. <br>" +
                    "서버에서는 요청 받은 토큰 정보를 토대로 회원 검증 및 리프레쉬 토큰 검증한 후 액세스 / 리프레시 토큰 재발급을 진행합니다")
    @PostMapping("/api/v1/refresh")
    public SingleResult<TokenDto> refresh(
            @Parameter(description = "재발급 요청을 할 토큰 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TokenRequestDto.class))) @RequestBody TokenRequestDto dto) {

        return responseService.getSingleResult(userJoinService.refresh(dto));
    }

    @Operation(summary = "[POST] 이메일 중복 여부 체크 요청",
            description = "회원가입 시 이메일 중복 체크 버튼을 누를 때 호출하는 API이며, 아래와 같이 호출해야 합니다. <br>" +
                    "[POST] /api/v1/emailCheck?email={email}")
    @PostMapping("/api/v1/emailCheck")
    public ResponseEntity emailCheck(@Parameter(description = "중복 체크가 필요한 이메일") @RequestParam String email) {

        Boolean result = userJoinService.emailDuplicateCheck(email);
        if (!result) {
            return ResponseEntity.ok().body(responseService.getSuccessResult());
        } // DB 조회 시 중복이 아닌 경우
        return ResponseEntity.badRequest()
                .body(responseService
                        .getFailResult(ErrorCode.EMAIL_DUPLICATED.getCode(), ErrorCode.EMAIL_DUPLICATED.getDescription())
                ); // DB 조회 시 중복인 경우
    }

    @Operation(summary = "[POST] 닉네임 중복 여부 체크 요청",
            description = "회원가입 시 닉네임 중복 체크 버튼을 누를 때 호출하는 API이며, 아래와 같이 호출해야 합니다. <br>" +
                    "[POST] /api/v1/nicknameCheck?nickname={nickname}")
    @PostMapping("/api/v1/nicknameCheck")
    public ResponseEntity nicknameCheck(@Parameter(description = "중복 체크가 필요한 닉네임") @RequestParam String nickname) {

        Boolean result = userJoinService.nicknameDuplicateCheck(nickname);
        if (!result) {
            return ResponseEntity.ok().body(responseService.getSuccessResult());
        } // DB 조회 시 중복이 아닌 경우
        return ResponseEntity.badRequest()
                .body(responseService
                        .getFailResult(ErrorCode.USER_NICKNAME_DUPLICATED.getCode(), ErrorCode.USER_NICKNAME_DUPLICATED.getDescription())
                ); // DB 조회 시 중복인 경우
    }

    @Operation(summary = "[POST] 소셜 로그인 연동 회원가입 요청",
            description = "소셜 로그인과 연동하여 회원가입을 요청합니다. 필요한 정보는 인가 코드, 닉네임입니다. <br>" +
                    "프론트엔드에서 소셜 로그인 연동 요청을 하여 인가 코드를 받아야 합니다. <br>" +
                    "연동에 성공하여 인가 코드를 받았다면, 회원가입 정보 폼에 인가 코드를 기입 해야 합니다. <br>" +
                    "소셜 로그인을 제공하는 인증 제공자는 provider로 구분합니다. <br><br>" +
                    "예외 상황 <br>" +
                    "소셜 로그인 연동 및 인가 코드를 받았다고 하더라도 소셜 로그인에 사용하는 이메일이 이미 서버 DB에 저장되어 있거나 <br>" +
                    "이미 소셜 로그인이 연동된 계정이거나 <br>" +
                    "가입 단계에서 닉네임이 중복인 경우, 가입이 불가능하며 이 경우 소셜 로그인 연동이 해제됩니다.")
    @PostMapping("/api/v1/oauth/join/{provider}")
    public SingleResult<String> joinBySocial(
            @Parameter(description = "회원 가입 요청 정보, 인가 코드(code)는 프론트엔드에서 주입하여야 합니다.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserSocialJoinRequestDto.class))) @Valid @RequestBody UserSocialJoinRequestDto dto,
            @Parameter(description = "인증 제공자, 카카오(kakao) / 네이버(naver) / 구글(google). 현재 카카오만 구현되어있습니다.")
            @PathVariable("provider") String provider) {

        // 인증 제공자 측으로 인가 코드를 통해 액세스 토큰 발급 요청
        OAuthAccessTokenDto accessToken = oAuthProviderService.getAcessToken(dto.getCode(), provider);

        // 액세스 토큰으로부터 프로필 정보 가져오기
        ProfileDto profile = oAuthProviderService.getProfile(accessToken.getAccess_token(), provider);

        // 프로필 정보를 가져오지 못하였으므로
        if (profile == null) throw new UserNotFoundApiException();

        // 프로필은 가져왔으나 이메일 동의를 얻지 못하였으므로
        if (profile.getEmail() == null) {
            if (provider.equals("kakao")) oAuthProviderService.kakaoUnlink(accessToken.getAccess_token());
            throw new OAuthAgreementException();
        }

        String username = userJoinService.joinBySocial(
                UserJoinRequestDto.builder()
                        .email(profile.getEmail())
                        .nickname(dto.getNickname())
                        .provider(provider)
                        .accessToken(accessToken.getAccess_token())
                        .build()
        );

        return responseService.getSingleResult(username);

    }

    @Operation(summary = "[POST] 소셜 로그인 연동 회원가입 후 로그인 요청",
            description = "소셜 로그인과 연동하여 회원가입이 완료되었을 경우 로그인을 요청합니다. 필요한 정보는 인가 코드입니다. <br>" +
                    "프론트엔드에서 소셜 로그인 연동 요청을 하여 인가 코드를 받아야 합니다. <br>" +
                    "연동에 성공하여 인가 코드를 받았다면, 로그인 요청 폼에 인가 코드를 기입 해야 합니다. <br>" +
                    "소셜 로그인을 제공하는 인증 제공자는 provider로 구분합니다.")
    @PostMapping("/api/v1/oauth/login/{provider}")
    public SingleResult<TokenDto> loginBySocial(
            @Parameter(description = "로그인 요청 정보, 인가 코드(code)는 프론트엔드에서 주입하여야 합니다." +
                    "로그인이 완료되면 액세스 토큰, 리프레시 토큰이 발급이 됩니다. 본 API 문서에서 인증이 필요한 API를 테스트하기 위해서는 <br>" +
                    "본 로그인 요청을 통해 전달 받은 액세스 토큰을 사용해야 합니다.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserSocialLoginRequestDto.class))) @RequestBody UserSocialLoginRequestDto dto,
            @Parameter(description = "인증 제공자, 카카오(kakao) / 네이버(naver) / 구글(google). 현재 카카오만 구현되어있습니다.")
            @PathVariable("provider") String provider) {

        TokenDto tokenDto = userJoinService.loginBySocial(dto, provider);

        return responseService.getSingleResult(tokenDto);

    }

    @Operation(summary = "[POST] 카카오 소셜 로그인 연동 해제 요청",
            description = "테스트용 API이며, 추후 회원 탈퇴 시에 활용될 수 있습니다. 인가 코드를 통해 전달 받는 액세스 토큰이 필요합니다. <br>" +
                    "소셜 로그인을 제공하는 인증 제공자는 provider로 구분합니다.")
    @PostMapping("/api/v1/oauth/unlink/{provider}")
    public CommonResult kakaoUnlink(@Parameter(description = "인증 제공자, 카카오(kakao) / 네이버(naver) / 구글(google). 현재 카카오만 구현되어있습니다.")
                                    @PathVariable("provider") String provider,
                                    @Parameter(description = "연결 해제가 필요한 사용자의 인가 코드") @RequestBody String code) {

        OAuthAccessTokenDto accessToken = oAuthProviderService.getAcessToken(code, provider);

        if (provider.equals("kakao")) oAuthProviderService.kakaoUnlink(accessToken.getAccess_token());

        return responseService.getSuccessResult();

    }
}
