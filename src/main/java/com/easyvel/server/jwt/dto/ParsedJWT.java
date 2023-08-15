package com.easyvel.server.jwt.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ParsedJWT {
    Map<String, String> header;
    Map<String, String> payload;
    String signature;
}
