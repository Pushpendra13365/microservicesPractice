package com.auth_service.exception;

public class CustomUnauthorizedException extends RuntimeException{
    public CustomUnauthorizedException(String message) {
        super(message);
    }
}
