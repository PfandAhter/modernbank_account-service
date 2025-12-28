package com.modernbank.account_service.rest.service.impl;

import com.modernbank.account_service.model.EncryptedCardData;
import com.modernbank.account_service.rest.service.CardNumberEncryptionService;
import com.modernbank.account_service.rest.service.CvvEncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardNumberEncryptionServiceImpl implements CardNumberEncryptionService {

    private final CvvEncryptionService encryptionService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public EncryptedCardData encryptCardNumber(String cardNumber) {
        return EncryptedCardData.builder()
                .lastFourDigits(cardNumber.substring(cardNumber.length() - 4))
                .encryptedFullNumber(encryptionService.encrypt(cardNumber))
                .cardNumberHash(passwordEncoder.encode(cardNumber))
                .build();
    }

    @Override
    public String decryptCardNumber(String encryptedNumber) {
        return encryptionService.decrypt(encryptedNumber);
    }
}