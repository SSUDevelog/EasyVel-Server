package com.easyvel.server.sign;

import com.easyvel.server.jwt.JWTParser;
import com.easyvel.server.jwt.JwtTokenProvider;
import com.easyvel.server.jwt.dto.ParsedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SignService {

    private final Logger LOGGER = LoggerFactory.getLogger(SignService.class);
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public boolean checkRegisterByUid(String uid) {
        Optional<User> user = userRepository.getByUid(uid);
        return user.isPresent();
    }

    public String getAppleId(String identity_token) throws JsonProcessingException, UnsupportedEncodingException {
        ParsedJWT parsedJWT = JWTParser.getParsedJWT(identity_token);

        return "apple_" + parsedJWT.getPayload().get("sub");
    }
}
