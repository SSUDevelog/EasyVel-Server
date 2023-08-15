package com.easyvel.server.config.security.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccessDeniedResponse {
    private String msg;
}
