package com.modernbank.account_service.rest.service.impl;

import com.modernbank.account_service.rest.service.CvvEncryptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
@Slf4j
public class CvvEncryptionServiceImpl implements CvvEncryptionService {

    private static final String ALGORITHM = "AES";

    @Value("${security.cvv-encryption.secret-key}")
    private String encryptionKey;

    @Override
    public String encrypt(String rawCvv) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(rawCvv.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("CVV encryption failed");
            throw new RuntimeException("Encryption failed"); //TODO: Custom Exception FIX THIS
        }
    }

    @Override
    public String decrypt(String encryptedCvv) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = Base64.getDecoder().decode(encryptedCvv);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            log.error("CVV decryption failed");
            throw new RuntimeException("Decryption failed"); //TODO: Custom Exception FIX THIS
        }
    }
}
