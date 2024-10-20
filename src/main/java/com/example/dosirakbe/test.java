package com.example.dosirakbe;

import com.example.dosirakbe.global.util.ApiExceptionEntity;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.ExceptionEnum;
import com.example.dosirakbe.global.util.StatusEnum;
import org.springframework.http.HttpStatus;

public class test {
    public static void main(String [] args){

        System.out.println(ApiResult.builder()
                .status(StatusEnum.SUCCESS)
                .message("")
                .data(" ")
                .exception(null)
                .build());
    }
}
