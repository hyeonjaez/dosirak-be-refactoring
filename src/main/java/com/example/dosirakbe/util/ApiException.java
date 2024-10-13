package com.example.dosirakbe.util;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private ExceptionEnum error;

    // Exception Enum 받아서
    public ApiException(ExceptionEnum e) {
        super(e.getMessage());
        this.error = e;
    }
}