package com.easyvel.server.jwt;

import com.easyvel.server.jwt.dto.ParsedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;


public class JWTParser {

    public static ParsedJWT getParsedJWT(String jwt) throws JsonProcessingException, UnsupportedEncodingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String[] splitJwt = jwt.split("\\.", 3);

        Map<String, String> header = objectMapper.readValue(new String(Base64.getDecoder().decode(splitJwt[0])), Map.class);
        Map<String, String> payload = objectMapper.readValue(new String(Base64.getDecoder().decode(splitJwt[1])), Map.class);
        String signature = splitJwt[2];

        return new ParsedJWT(header, payload, signature);
    }
}
