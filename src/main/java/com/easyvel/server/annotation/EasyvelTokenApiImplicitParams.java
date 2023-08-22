package com.easyvel.server.annotation;

import com.easyvel.server.config.security.SecurityConfiguration;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiImplicitParams({
        @ApiImplicitParam(name = SecurityConfiguration.TOKEN_HEADER, value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")})
public @interface EasyvelTokenApiImplicitParams {
}