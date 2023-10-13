package com.easyvel.server.sign.google.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class GetTokensRequestBody {
    String client_id;
    String client_secret;
    String code;
    String grant_type;
    String redirect_uri;
}
