package com.codesoom.demo.controllers;

import com.codesoom.demo.dto.ErrorResponse;
import com.codesoom.demo.errors.InvalidTokenException;
import com.codesoom.demo.errors.LoginFailException;
import com.codesoom.demo.errors.ProductNotFoundException;
import com.codesoom.demo.errors.UserEmailDuplicationExeption;
import com.codesoom.demo.errors.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class ControllerErrorAdvice {


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException.class)
    public ErrorResponse handleTaskNotFound(){
        return new ErrorResponse("Task not found");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFound(){
        return new ErrorResponse("Product not found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailDuplicationExeption.class)
    public ErrorResponse handleUserEamilAlreayExisted(){
        return new ErrorResponse("User's email address is already existed");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound(){
        return new ErrorResponse("User not found");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidTokenException.class)
    public ErrorResponse handleInvalidAcceesTokenException(){
        return new ErrorResponse("Invalid access token");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginFailException.class)
    public ErrorResponse handleLoginFailException(){
        return new ErrorResponse("Log-in failed");
    }
}
