package com.example.dosirakbe.global.util;

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
public final class EmptyResponse {

    private static class EmptyResponseInstanceHolder {
        private static final EmptyResponse INSTANCE = new EmptyResponse();
    }

    public static EmptyResponse getInstance() {
        return EmptyResponseInstanceHolder.INSTANCE;
    }

    private EmptyResponse() {
    }
}
