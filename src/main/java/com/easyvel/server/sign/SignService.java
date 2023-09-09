package com.easyvel.server.sign;

import com.easyvel.server.exception.SignException;
import com.easyvel.server.global.entity.User;
import com.easyvel.server.global.repository.UserRepository;
import com.easyvel.server.jwt.JWTParser;
import com.easyvel.server.jwt.JwtTokenProvider;
import com.easyvel.server.jwt.dto.ParsedJWT;
import com.easyvel.server.sign.dto.SignInDto;
import com.easyvel.server.sign.dto.SignUpDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SignService {

    private final Logger LOGGER = LoggerFactory.getLogger(SignService.class);
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public boolean checkRegisteredByUid(String uid) {
        Optional<User> user = userRepository.getByUid(uid);
        return user.isPresent();
    }

    //Todo: 임시구현
    public String getGoogleID(String identity_token) throws JsonProcessingException, UnsupportedEncodingException {
        ParsedJWT parsedJWT = JWTParser.getParsedJWT(identity_token);

        return "google_" + parsedJWT.getPayload().get("sub");
    }

    public String getAppleId(String identity_token) throws JsonProcessingException, UnsupportedEncodingException {
        ParsedJWT parsedJWT = JWTParser.getParsedJWT(identity_token);

        return "apple_" + parsedJWT.getPayload().get("sub");
    }

    public void signUp(SignUpDto signUpDto) throws SignException {

        LOGGER.info("[signUp] 중복 검사");
        Optional<User> optionalUser = userRepository.getByUid(signUpDto.getId());
        if (optionalUser.isPresent())
            throw new SignException(HttpStatus.BAD_REQUEST, "중복된 ID 입니다.");

        LOGGER.info("[signUp] User 엔티티 생성");
        User user = createUser(signUpDto);

        LOGGER.info("[signUp] userRepository 저장");
        User savedUser = userRepository.save(user);

        if (savedUser.getName().isEmpty())
            throw new SignException(HttpStatus.BAD_REQUEST, "DB 저장에 실패했습니다.");
    }

    /**
     * @param signInDto
     * @return token
     * @throws SignException
     */
    public String signIn(SignInDto signInDto) throws SignException {
        LOGGER.info("[signIn] 회원 정보 확인");
        Optional<User> optionalUser = userRepository.getByUid(signInDto.getId());
        if (optionalUser.isEmpty())
            throw new SignException(HttpStatus.BAD_REQUEST, "아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다.");

        User user = optionalUser.get();
        LOGGER.info("[signIn] Id : {}", signInDto.getId());

        LOGGER.info("[signIn] 패스워드 비교 수행");
        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword()))
            throw new SignException(HttpStatus.BAD_REQUEST, "아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다.");

        user.setFcmToken(signInDto.getFcmToken());

        LOGGER.info("[signIn] fcmToken 저장");
        User savedUser = userRepository.save(user);
        if (!savedUser.getFcmToken().equals(signInDto.getFcmToken()))
            throw new SignException(HttpStatus.INTERNAL_SERVER_ERROR, "fcmToken 저장에 실패했습니다.");

        return makeToken(user);
    }

    public void signOut(String uid) throws SignException {
        LOGGER.info("[signOut] 회원 탈퇴 정보 확인");
        Optional<User> optionalUser = userRepository.getByUid(uid);

        if (optionalUser.isEmpty())
            throw new SignException(HttpStatus.BAD_REQUEST, "존재하지 않는 아이디입니다.");

        User user = optionalUser.get();
        LOGGER.info("[getSignOutResult] userRepository 삭제");
        userRepository.delete(user);
    }

    public String makeTokenByUid(String uid) throws SignException {
        Optional<User> optionalUser = userRepository.getByUid(uid);

        if (optionalUser.isEmpty())
            throw new SignException(HttpStatus.BAD_REQUEST, "존재하지 않는 아이디입니다.");

        return makeToken(optionalUser.get());
    }

    public String makeToken(User user) throws SignException {
        if (user == null)
            throw new SignException(HttpStatus.BAD_REQUEST, "아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다.");

        return jwtTokenProvider.createToken(String.valueOf(user.getUid()), user.getRoles());
    }

    private User createUser(SignUpDto signUpDto) {
        User user = User.builder()
                .uid(signUpDto.getId())
                .name(signUpDto.getName())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        return user;
    }
}
