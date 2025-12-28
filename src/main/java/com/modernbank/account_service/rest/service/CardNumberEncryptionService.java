package com.modernbank.account_service.rest.service;

import com.modernbank.account_service.model.EncryptedCardData;

public interface CardNumberEncryptionService {
    EncryptedCardData encryptCardNumber(String cardNumber);

    String decryptCardNumber(String encryptedNumber);
}
