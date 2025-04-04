package com.example.dosirakbe.global.util;

import java.util.Objects;

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
public final class ObjectsUtility {
    public static void checkAllNotNull(Object... objects) {
        for (Object object : objects) {
            checkNotNull(object);
        }
    }

    private static <T> void checkNotNull(T object) {
        if (Objects.isNull(object)) {
            throw new NullPointerException("해당 파라미터의 값은 null 이면 안됩니다.");
        }
    }

    private ObjectsUtility() {
        throw new AssertionError("Utility class cannot be instantiated");
    }
}
