package com.easyvel.server.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class BaseResponse<T> {

    private final boolean success;
    private final int code;
    private final String message;
    private final T result;


    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(true, 200, null, null);
    }

    public static <T> BaseResponse<T> success(String message) {
        return new BaseResponse<>(true, 200, message, null);
    }

    public static <T> BaseResponse<T> success(String message, T result) {
        return new BaseResponse<>(true, 200, message, result);
    }

    public static <T> BaseResponse<T> fail(int httpStatus, String message) {
        return new BaseResponse<>(false, httpStatus, message, null);
    }

    public static <T> BaseResponse<T> fail(int httpStatus, String message, T result) {
        return new BaseResponse<>(false, httpStatus, message, result);
    }
}