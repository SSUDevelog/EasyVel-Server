package com.easyvel.server.sign.apple.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class GetAccessTokenRequestBody {
    String client_id;
    String client_secret;
    String grant_type;
    String refresh_token;
}
