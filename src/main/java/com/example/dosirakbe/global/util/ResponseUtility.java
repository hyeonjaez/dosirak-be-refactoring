package com.example.dosirakbe.global.util;

import com.example.dosirakbe.global.api.ApiResponse;
import com.example.dosirakbe.global.api.StatusEnum;
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
public final class ResponseUtility {

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(new ApiResponse<>(StatusEnum.SUCCESS, message, data));
    }

    public static ResponseEntity<ApiResponse<EmptyResponse>> successResponseDataEmpty(String message) {
        return ResponseEntity.ok(new ApiResponse<>(StatusEnum.SUCCESS, message, EmptyResponse.getInstance()));
    }


    private ResponseUtility() {

        throw new UnsupportedOperationException("Utility Class");
    }
}
