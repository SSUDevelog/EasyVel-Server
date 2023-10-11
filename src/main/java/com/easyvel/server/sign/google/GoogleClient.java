package com.easyvel.server.sign.google;

import com.easyvel.server.sign.google.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GoogleClient {
    @Value("${google.auth.url}")
    private String googleAuthUrl;

    public GetGoogleTokenResponse getTokensResponse(GetTokensRequestBody getTokensRequestBody) throws JsonProcessingException {
        GetGoogleTokenResponse getGoogleTokenResponse = new GetGoogleTokenResponse();
        ObjectMapper objectMapper = new ObjectMapper();

        WebClient webClient = WebClient.builder()
                .baseUrl(googleAuthUrl)
                .build();

        ResponseEntity<String> responseEntity = webClient.post()
                .uri("/token")
                .header("content-type: application/x-www-form-urlencoded")
                .bodyValue(getTokensRequestBody)
                .retrieve()
                .toEntity(String.class)
                .block();

        getGoogleTokenResponse.setHttpStatus(responseEntity.getStatusCode());
        if (getGoogleTokenResponse.getHttpStatus().equals(HttpStatus.OK)) {
            getGoogleTokenResponse.setTokenResponse(objectMapper.readValue(responseEntity.getBody(), GetGoogleTokenResponse.TokenResponse.class));
        }

        return getGoogleTokenResponse;
    }
}
