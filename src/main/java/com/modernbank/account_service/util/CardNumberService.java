package com.modernbank.account_service.util;

import com.modernbank.account_service.model.enums.CardNetwork;
import com.modernbank.account_service.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class CardNumberService {

    private final CardRepository cardRepository;

    private static final SecureRandom random = new SecureRandom();

    @Transactional
    public String generateUniqueCardNumber(CardNetwork network) {
        String cardNumber;
        do {
            cardNumber = generateCardNumber(network);
        } while (cardRepository.existsByCardNumber(cardNumber));
        return cardNumber;
    }

    private String generateCardNumber(CardNetwork network) {
        String bin = network.getBinPrefix();
        int length = network.getLength();

        StringBuilder builder = new StringBuilder(bin);
        while (builder.length() < length - 1) {
            builder.append(random.nextInt(10));
        }

        int checkDigit = calculateLuhnCheckDigit(builder.toString());
        builder.append(checkDigit);

        return builder.toString();
    }

    private int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = true;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        return (10 - (sum % 10)) % 10;
    }
}