package com.modernbank.account_service.rest.service;

public interface CvvEncryptionService {
    String encrypt(String rawCvv);
    String decrypt(String encryptedCvv);
}