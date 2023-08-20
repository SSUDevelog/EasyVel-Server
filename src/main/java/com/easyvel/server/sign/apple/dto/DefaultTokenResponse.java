package com.easyvel.server.sign.apple.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
public class DefaultTokenResponse {
    HttpStatus httpStatus;

    TokenResponse tokenResponse;
    ErrorResponse errorResponse;

    @Getter
    @Setter
    @ToString
    public static class TokenResponse {
        String access_token;
        Long expires_in;
        String id_token;
        String refresh_token;
        String token_type;
    }

    @Getter
    @Setter
    @ToString
    public static class ErrorResponse {
        String error;
    }
}
