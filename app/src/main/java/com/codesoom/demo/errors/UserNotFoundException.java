package com.codesoom.demo.errors;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id){
        super("User not found : " + id);
    }
}
