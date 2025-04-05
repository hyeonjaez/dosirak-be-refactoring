package com.example.dosirakbe.global.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : com.example.dosirakbe.global.api<br>
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
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(StatusEnum status, String message, T data) {
    @Builder
    public ApiResponse {
    }
}
