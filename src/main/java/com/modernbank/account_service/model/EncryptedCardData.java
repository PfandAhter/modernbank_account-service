package com.modernbank.account_service.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EncryptedCardData {

    private String lastFourDigits;
    private String encryptedFullNumber;
    private String cardNumberHash;
}