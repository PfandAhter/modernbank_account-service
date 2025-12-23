package com.modernbank.account_service.exception;

import java.time.LocalDateTime;

/**
 * Exception thrown when an account is temporarily blocked due to fraud
 * activity.
 * The block is time-based and will automatically expire.
 */
public class AccountBlockedException extends BusinessException {

    private final String iban;
    private final LocalDateTime blockedUntil;

    public AccountBlockedException(String iban, LocalDateTime blockedUntil) {
        super(String.format("Account with IBAN %s is temporarily blocked until %s due to suspicious activity",
                iban, blockedUntil));
        this.iban = iban;
        this.blockedUntil = blockedUntil;
    }

    public String getIban() {
        return iban;
    }

    public LocalDateTime getBlockedUntil() {
        return blockedUntil;
    }
}
