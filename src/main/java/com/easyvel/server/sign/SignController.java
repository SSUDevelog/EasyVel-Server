package com.easyvel.server.sign;

import com.easyvel.server.annotation.EasyvelTokenApiImplicitParams;
import com.easyvel.server.config.security.SecurityConfiguration;
import com.easyvel.server.exception.SignException;
import com.easyvel.server.jwt.JwtTokenProvider;
import com.easyvel.server.sign.apple.AppleAuthService;
import com.easyvel.server.sign.apple.dto.GetTokensDto;
import com.easyvel.server.sign.dto.SignInDto;
import com.easyvel.server.sign.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sign")
public class SignController {

    private final Logger LOGGER = LoggerFactory.getLogger(SignController.class);
    private final SignService signService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AppleAuthService appleAuthService;

    @EasyvelTokenApiImplicitParams
    @GetMapping("/token/refresh")
    public String refreshToken(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token) throws SignException {
        String uid = jwtTokenProvider.getUid(token);

        return signService.makeTokenByUid(uid);
    }

    /**
     * @param getTokensDto
     * @return token
     * @throws Exception
     */
    @PostMapping("/apple-login")
    public String appleLogin(@Validated @RequestBody GetTokensDto getTokensDto) throws Exception {
        LOGGER.info("[apple-login] 애플 로그인을 수행합니다.");
        appleAuthService.checkIdentityToken(getTokensDto.getIdentity_token());
        LOGGER.info("[apple-login] identityToken 검정 완료");
        String uid = signService.getAppleId(getTokensDto.getIdentity_token());
        LOGGER.info("[apple-login] uid: {}", uid);
        //Todo: 임시 값 바꾸기
        String password = "password";

        if (!signService.checkRegisteredByUid(uid)) {
            LOGGER.info("[apple-login] 계정 생성");
            SignUpDto signUpDto = new SignUpDto(uid, password, "new user");
            //즉시반영이 안되는 문제
            signService.signUp(signUpDto);
            LOGGER.info("[apple-login] 계정 생성 성공");
        }

        LOGGER.info("[apple-login] 로그인 진행");
        //Todo: 임시로 fcm 값을 넣었음
        SignInDto signInDto = new SignInDto(uid, password, "fcmToken");
        return signIn(signInDto);
    }

    @PostMapping("/sign-up")
    public void signUp(
            @Validated @RequestBody SignUpDto signUpDto) throws SignException{
        LOGGER.info("[signUp] 회원가입을 수행합니다. id : {}, pw : ****, name : {}, role : {}", signUpDto.getId(), signUpDto.getName());
        signService.signUp(signUpDto);

        LOGGER.info("[signUp] 회원가입을 완료했습니다. id : {}", signUpDto.getId());
    }

    /**
     * @param signInDto
     * @return token
     * @throws SignException
     */
    @PostMapping("/sign-in")
    public String signIn(
            @Validated @RequestBody SignInDto signInDto) throws SignException {
        LOGGER.info("[signIn] 로그인을 시도하고 있습니다. id : {}, pw : ****", signInDto.getId());
        String token = signService.signIn(signInDto);

        /*Todo: 나중에 알림 추가 시 활성화
        JoinGroupDto joinGroupDto = new JoinGroupDto(signInDto.getId(), "AllGroup");
        notificationService.joinGroup(joinGroupDto);//로그아웃할때는 제거하는 기능 추가하기
         */
        return token;
    }

    @EasyvelTokenApiImplicitParams
    @PostMapping("/sign-out")
    public void signOut(@RequestHeader("X-AUTH-TOKEN") String token) throws SignException {
        String uid = jwtTokenProvider.getUid(token);
        LOGGER.info("[signIn] 회원탈퇴를 시도하고 있습니다. id : {}, pw : ****", uid);

        signService.signOut(uid);

        LOGGER.info("[signIn] 정상적으로 회원 탈퇴 되었습니다. id : {}, token : {}", uid);
    }
}
