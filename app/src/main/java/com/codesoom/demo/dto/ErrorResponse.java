package com.codesoom.demo.dto;

//DTO는 클라이언트랑 서버랑 데이터 주고받음
public class ErrorResponse {
    private String message;
    public ErrorResponse(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
