package com.easyvel.server.subscribe.dto;

import lombok.Data;

@Data
public class UserMainDto {
    private String userMainUrl;
    public UserMainDto(String name) {
        this.userMainUrl = "https://velog.io/@" + name + "/about";
    }
}
