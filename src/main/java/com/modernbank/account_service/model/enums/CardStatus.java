package com.modernbank.account_service.model.enums;

public enum CardStatus {
    ACTIVE("ACTIVE"),
    BLOCKED("BLOCKED"),
    EXPIRED("EXPIRED"),
    PREPARING("PREPARING"),
    PENDING_APPROVAL("PENDING_APPROVAL"),
    CANCELLED("CANCELLED");

    private final String status;

    CardStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}