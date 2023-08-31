package com.easyvel.server.subscribe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateVelogUserDto {
    private Boolean validate;
    private String userName;
    private String profilePictureURL;
    private String profileURL;

    public ValidateVelogUserDto(String userName) {
        this.userName = userName;
        this.profileURL = "https://velog.io/@" + userName;
    }
}
