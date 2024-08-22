package com.fastcampus.befinal.common.annotation;

import com.fastcampus.befinal.common.response.error.info.JwtErrorCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SwaggerErrorCodeExamples {
    JwtErrorCode[] value();
}
