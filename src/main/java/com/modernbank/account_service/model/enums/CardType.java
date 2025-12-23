package com.modernbank.account_service.model.enums;

public enum CardType {
    DEBIT("DEBIT"),
    CREDIT("CREDIT");

    private final String type;

    CardType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}