package com.example.dosirakbe.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.processing.Generated;

/**
 * packageName    : com.example.dosirakbe.global.exception<br>
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
@AllArgsConstructor
public class ApiException extends RuntimeException {
    private final ErrorCode errorCode;
}
