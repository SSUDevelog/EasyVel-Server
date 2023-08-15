package com.easyvel.server.config.security;

import com.easyvel.server.config.security.dto.AccessDeniedResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(BaseAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        LOGGER.info("[commence] 인가 실패로 response.sendError 발생");

        AccessDeniedResponse accessDeniedResponse = new AccessDeniedResponse();
        accessDeniedResponse.setMsg("권한이 없습니다.");

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(accessDeniedResponse));
    }
}
