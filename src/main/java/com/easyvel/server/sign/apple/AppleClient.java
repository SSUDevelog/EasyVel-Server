package com.easyvel.server.sign.apple;

import com.easyvel.server.sign.apple.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AppleClient {

    @Value("${apple.auth.url}")
    private String appleAuthUrl;

    /**
     *
     * refer to below
     * https://developer.apple.com/documentation/sign_in_with_apple/fetch_apple_s_public_key_for_verifying_token_signature
     * @return 200 OK return JWKSet 400 Bad Request return null
     */
    public JWKSet getJWKSet() {
        WebClient webClient = WebClient.builder()
                .baseUrl(appleAuthUrl)
                .build();

        ResponseEntity<JWKSet> responseEntity = webClient.get()
                .uri("/keys")
                .retrieve()
                .toEntity(JWKSet.class)
                .block();

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity.getBody();
        }
        return null;
    }

    public GetTokensResponse getTokensResponse(GetTokensRequestBody getTokensRequestBody) throws JsonProcessingException {
        GetTokensResponse getTokensResponse = new GetTokensResponse();
        ObjectMapper objectMapper = new ObjectMapper();

        WebClient webClient = WebClient.builder()
                .baseUrl(appleAuthUrl)
                .build();

        ResponseEntity<String> responseEntity = webClient.post()
                .uri("/token")
                .header("content-type: application/x-www-form-urlencoded")
                .bodyValue(getTokensRequestBody)
                .retrieve()
                .toEntity(String.class)
                .block();

        getTokensResponse.setHttpStatus(responseEntity.getStatusCode());
        if (getTokensResponse.getHttpStatus().equals(HttpStatus.OK)) {
            getTokensResponse.setTokenResponse(objectMapper.readValue(responseEntity.getBody(), GetTokensResponse.TokenResponse.class));
        } else {
            getTokensResponse.setErrorResponse(objectMapper.readValue(responseEntity.getBody(), GetTokensResponse.ErrorResponse.class));
        }
        return getTokensResponse;
    }

    public GetAccessTokenResponse getAccessTokenResponse(GetAccessTokenRequestBody getAccessTokenRequestBody) throws JsonProcessingException {
        GetAccessTokenResponse getAccessTokenResponse = new GetAccessTokenResponse();
        ObjectMapper objectMapper = new ObjectMapper();

        WebClient webClient = WebClient.builder()
                .baseUrl(appleAuthUrl)
                .build();

        ResponseEntity<String> responseEntity = webClient.post()
                .uri("/token")
                .header("content-type: application/x-www-form-urlencoded")
                .bodyValue(getAccessTokenRequestBody)
                .retrieve()
                .toEntity(String.class)
                .block();

        getAccessTokenResponse.setHttpStatus(responseEntity.getStatusCode());
        if (getAccessTokenResponse.getHttpStatus().equals(HttpStatus.OK)) {
            getAccessTokenResponse.setTokenResponse(objectMapper.readValue(responseEntity.getBody(), GetAccessTokenResponse.TokenResponse.class));
        } else {
            getAccessTokenResponse.setErrorResponse(objectMapper.readValue(responseEntity.getBody(), GetAccessTokenResponse.ErrorResponse.class));
        }
        return getAccessTokenResponse;
    }
}
