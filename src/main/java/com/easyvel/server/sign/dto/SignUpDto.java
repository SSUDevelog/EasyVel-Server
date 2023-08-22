package com.easyvel.server.sign.dto;


import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {

    @ApiParam(value = "ID", required = true)
    @NotBlank
    private String id;

    @ApiParam(value = "비밀번호", required = true)
    @NotBlank
    private String password;

    @ApiParam(value = "이름", required = true)
    @NotBlank
    private String name;
}
