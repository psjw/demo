package com.codesoom.demo.controllers;

import com.codesoom.demo.dto.ErrorResponse;
import com.codesoom.demo.errors.ProductNotFoundException;
import com.codesoom.demo.errors.UserEmailDuplicationExeption;
import com.codesoom.demo.errors.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerErrorAdvice {

    @ResponseBody // 써 놓은 것 그대로 나감
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException.class)
    public ErrorResponse handleTaskNotFound(){
        return new ErrorResponse("Task not found");
    }

    @ResponseBody // 써 놓은 것 그대로 나감
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFound(){
        return new ErrorResponse("Product not found");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailDuplicationExeption.class)
    public ErrorResponse handleUserEamilAlreayExisted(){
        return new ErrorResponse("User's email address is already existed");
    }

    @ResponseBody // 써 놓은 것 그대로 나감
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound(){
        return new ErrorResponse("User not found");
    }

}
