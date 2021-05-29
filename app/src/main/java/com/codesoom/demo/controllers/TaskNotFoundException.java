package com.codesoom.demo.controllers;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(Long id){
        super("Task not found : " + id);
    }
}
