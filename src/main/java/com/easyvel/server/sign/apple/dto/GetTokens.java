package com.easyvel.server.sign.apple.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class GetTokens {
    String access_token;
    String refresh_token;
}
