package com.modernbank.account_service.model.enums;

public enum CardNetwork {
    // X27922
    VISA("427922", 16),
    MASTERCARD("527922", 16),
    TROY("927922", 16),
    AMERICAN_EXPRESS("327922", 15);

    private final String binPrefix;
    private final int length;

    CardNetwork(String binPrefix, int length) {
        this.binPrefix = binPrefix;
        this.length = length;
    }

    public String getBinPrefix() {
        return binPrefix;
    }

    public int getLength() {
        return length;
    }
}