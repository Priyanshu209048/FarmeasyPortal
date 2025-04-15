package com.project.farmeasyportal.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public RuntimeException resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        return new RuntimeException(ex.getMessage());
    }

}
