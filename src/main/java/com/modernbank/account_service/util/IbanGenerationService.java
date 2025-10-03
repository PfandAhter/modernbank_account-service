package com.modernbank.account_service.util;

import com.modernbank.account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IbanGenerationService {
    private static final String COUNTRY_CODE = "TR";
    private static final String BANK_CODE = "12345";
    private static final String RESERVE = "0";

    private final AccountRepository accountRepository;

    public String generateUniqueIban() {
        String iban;

        do {
            String accountNumber = generateRandomDigits(16);
            String bban = BANK_CODE + RESERVE + accountNumber;

            String convertedCountryCode = "292700"; // T=29, R=27, 00
            String ibanNumeric = bban + convertedCountryCode;

            int checkDigits = 98 - mod97(ibanNumeric);
            String formattedCheckDigits = String.format("%02d", checkDigits);
            iban = COUNTRY_CODE + formattedCheckDigits + bban;

        } while (accountRepository.existsByIban(iban));

        return iban;
    }

    private String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    private int mod97(String input) {
        BigInteger bigInt = new BigInteger(input);
        return bigInt.mod(BigInteger.valueOf(97)).intValue();
    }
}