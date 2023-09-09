package com.easyvel.server.config.security;

import com.easyvel.server.global.dto.DefaultResponse;
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
        LOGGER.info("[commence] 인가 실패로 response.sendError 발생");

        DefaultResponse defaultResponse = new DefaultResponse("권한이 없습니다.");
        defaultResponse.setResponse(response, HttpStatus.FORBIDDEN);
    }
}
