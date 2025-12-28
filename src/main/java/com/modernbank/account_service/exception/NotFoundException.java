package com.modernbank.account_service.exception;

import lombok.Getter;

public class NotFoundException extends BusinessException{

    @Getter
    private String message;

    public NotFoundException(String message){
        super(message);
        this.message = message;
    }
}