package com.easyvel.server.config.security;

import com.easyvel.server.global.dto.DefaultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class BaseAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final Logger LOGGER = LoggerFactory.getLogger(BaseAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        LOGGER.info("[commence] 인증 실패로 response.sendError 발생");

        DefaultResponse defaultResponse = new DefaultResponse("인증이 실패하였습니다.");
        defaultResponse.setResponse(response, HttpStatus.UNAUTHORIZED);
    }
}
