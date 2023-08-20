package com.easyvel.server.sign.apple.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetTokensDto {
    String identity_token;
    String authorization_code;
}
