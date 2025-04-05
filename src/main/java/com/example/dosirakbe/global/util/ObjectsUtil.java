package com.example.dosirakbe.global.util;

import com.example.dosirakbe.global.exception.CustomException;
import com.example.dosirakbe.global.exception.CommonErrorCode;

import java.util.Arrays;
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
public final class ObjectsUtil {
    public static void checkAllNotNull(Object... objects) {
        Arrays.stream(objects)
                .forEach(ObjectsUtil::checkNotNull);
    }

    public static boolean isNull(Object object) {
        return Objects.isNull(object);
    }

    public static void checkIdIntegers(Integer... ids) {
        Arrays.stream(ids).forEach(ObjectsUtil::checkIdIntegerValid);
    }

    public static void checkIdLongs(Long... ids) {
        Arrays.stream(ids).forEach(ObjectsUtil::checkIdLongValid);
    }

    private static void checkIdIntegerValid(Integer id) {
        checkNotNull(id);
        if (id <= 0) {
            throw new CustomException(CommonErrorCode.PARAMETER_ID_VALUE);
        }
    }

    private static void checkIdLongValid(Long id) {
        checkNotNull(id);
        if (id <= 0) {
            throw new CustomException(CommonErrorCode.PARAMETER_ID_VALUE);
        }
    }

    private static <T> void checkNotNull(T object) {
        if (Objects.isNull(object)) {
            throw new CustomException(CommonErrorCode.PARAMETER_NULL);
        }
    }

    private ObjectsUtil() {
        throw new AssertionError("Utility class cannot be instantiated");
    }
}
