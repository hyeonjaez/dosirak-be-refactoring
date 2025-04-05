package com.example.dosirakbe.global.util;

import com.example.dosirakbe.global.common.api.ApiResponse;
import com.example.dosirakbe.global.common.api.StatusEnum;
import com.example.dosirakbe.global.common.dto.EmptyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * packageName    : com.example.dosirakbe.global.util<br>
 * fileName       : null.java<br>
 * author         : SSAFY<br>
 * date           : 2025-04-04<br>
 * description    :  <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * <br>
 * <br>
 */
public final class ResponseEntityUtil {

    public static <T> ResponseEntity<ApiResponse<T>> successOk(String message, T data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(StatusEnum.SUCCESS, message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> successCreated(String message, T data) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(StatusEnum.SUCCESS, message, data));
    }

    public static ResponseEntity<ApiResponse<EmptyResponse>> successResponseDataEmpty(String message) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(StatusEnum.SUCCESS, message, EmptyResponse.getInstance()));
    }


    private ResponseEntityUtil() {
        throw new UnsupportedOperationException("Utility Class");
    }
}
