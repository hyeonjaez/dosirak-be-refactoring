package com.example.dosirakbe.global.util;

import org.springframework.validation.BindingResult;

public class ValidationUtils {

    public static void validationRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationFailedException(bindingResult);
        }
    }

    private ValidationUtils() {
    }
}
