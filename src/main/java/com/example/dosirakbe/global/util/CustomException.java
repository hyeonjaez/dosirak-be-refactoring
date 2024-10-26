package com.example.dosirakbe.global.util;



import com.example.dosirakbe.global.util.ExceptionEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final ExceptionEnum exceptionEnum;

    public CustomException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.exceptionEnum = exceptionEnum;
    }

    public HttpStatus getStatus() {
        return exceptionEnum.getStatus();
    }

    public String getCode() {
        return exceptionEnum.getCode();
    }
}
