package com.codesoom.demo.errors;

public class UserEmailDuplicationExeption extends RuntimeException{
    public UserEmailDuplicationExeption(String email){
        super("User email is already existed : " + email);
    }
}
