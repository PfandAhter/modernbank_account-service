package com.modernbank.account_service.model.enums;

public enum OTPType {
    CREATION("CREATION"),
    AUTHENTICATION("AUTHENTICATION"),
    TRANSACTION("TRANSACTION"),
    PASSWORD_RESET("PASSWORD_RESET");


    private final String type;
    OTPType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}