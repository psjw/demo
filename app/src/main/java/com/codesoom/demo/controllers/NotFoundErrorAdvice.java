package com.codesoom.demo.controllers;

import com.codesoom.demo.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NotFoundErrorAdvice {

    @ResponseBody // 써 놓은 것 그대로 나감
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException.class)
    public ErrorResponse handleNotFound(){
        return new ErrorResponse("Task not found");
    }
}
