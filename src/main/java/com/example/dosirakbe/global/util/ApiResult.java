package com.example.dosirakbe.global.util;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ApiResult<T>{
    private StatusEnum status;
    private String message;
    private T data;
    private ApiExceptionEntity exception;


    @Builder
    public ApiResult(StatusEnum status, String message, T data, ApiExceptionEntity exception) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.exception = exception;
    }

}