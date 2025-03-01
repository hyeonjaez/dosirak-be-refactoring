package com.example.dosirakbe.global.api;

import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.ExceptionEnum;
import com.example.dosirakbe.global.util.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class ApiResponseAspect {

    @Around("@annotation(com.example.dosirakbe.global.api.ApiResponseWrapper)")
    public ResponseEntity<?> wrapApiResponse(ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            ApiResponseWrapper annotation = method.getAnnotation(ApiResponseWrapper.class);

            Object result = joinPoint.proceed();

            HttpStatus status = annotation.status();
            String message = annotation.message();

            ApiResult<Object> apiResult = ApiResult.<Object>builder()
                    .status(StatusEnum.SUCCESS)
                    .message(message)
                    .data(result)
                    .build();

            return ResponseEntity.status(status).body(apiResult);
        } catch (Throwable e) {
            log.error("Error in API response handling", e);
            throw new ApiException(ExceptionEnum.INTERNAL_SERVER_ERROR);
        }
    }
}
