package com.example.dosirakbe.global.api;

import org.springframework.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiResponseWrapper {
    HttpStatus status() default HttpStatus.OK;

    String message() default "Request processed successfully";
}