package com.easyvel.server.sign.google;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import com.easyvel.server.sign.google.dto.*;
import com.easyvel.server.sign.google.exception.*;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {
    @Value("${google.auth.client_id}")
    private String client_id;

    @Value("${google.auth.client_secret}")
    private String client_secret;

    @Value("${google.auth.redirect_uri}")
    private String redirect_uri;

    private final GoogleClient googleClient;

    public GetGoogleTokenResponse getTokensResponse(String code) throws GoogleAuthServiceException, JsonProcessingException {
        GetTokensRequestBody getTokensRequestBody = new GetTokensRequestBody(client_id , client_secret, code, "authorization_code", redirect_uri);
        GetGoogleTokenResponse getGoogleTokenResponse = googleClient.getTokensResponse(getTokensRequestBody);
        if (getGoogleTokenResponse.getHttpStatus().equals(HttpStatus.BAD_REQUEST))
            throw new GoogleAuthServiceException("구글 인증에 실패하였습니다.");

        return getGoogleTokenResponse;
    }
}
