package com.easyvel.server.sign.dto;

import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInDto {

    @ApiParam(value = "ID", required = true)
    @NotBlank
    private String id;

    @ApiParam(value = "Password", required = true)
    @NotBlank
    private String password;

    @ApiParam(value = "FCMToken", required = true)
    @NotBlank
    private String fcmToken;
}