package com.easyvel.server.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = SignException.class)
    public ResponseEntity<Map<String, String>> SignExceptionHandler(SignException e) {
        LOGGER.error("SignExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        return makeResponseEntity(e.getHttpStatus(), e.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        LOGGER.error("Global - RuntimeException, {}, {}", request.getRequestURI(), e.getMessage());

        return makeResponseEntity(httpStatus, e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, String>> handleIOException(IOException e, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        LOGGER.error("Global - IOException, {}, {}", request.getRequestURI(), e.getMessage());

        return makeResponseEntity(httpStatus, e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        LOGGER.error("Global - Exception, {}, {}", request.getRequestURI(), e.getMessage());

        return makeResponseEntity(httpStatus, e.getMessage());
    }

    private ResponseEntity<Map<String, String>> makeResponseEntity(HttpStatus httpStatus, String message) {
        HttpHeaders responseHeaders = new HttpHeaders();

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", Integer.toString(httpStatus.value()));
        map.put("message", message);

        return new ResponseEntity<>(map, responseHeaders, httpStatus);
    }
}
