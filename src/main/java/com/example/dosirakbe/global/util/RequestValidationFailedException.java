package com.example.dosirakbe.global.util;

import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RequestValidationFailedException extends ApiException {

    private final BindingResult bindingResult;

    public RequestValidationFailedException(BindingResult bindingResult) {
        super(ExceptionEnum.RUNTIME_EXCEPTION);
        this.bindingResult = bindingResult;
    }

    @Override
    public String getMessage() {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream()
                .map(error ->
                        String.format("Field: %s, Rejected value: %s, Message: %s",
                                error.getField(),
                                error.getRejectedValue(),
                                error.getDefaultMessage()))
                .collect(Collectors.joining("; "));
    }
}