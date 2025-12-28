package com.modernbank.account_service.api.dto;

import com.modernbank.account_service.model.enums.CardNetwork;
import com.modernbank.account_service.model.enums.CardStatus;
import com.modernbank.account_service.model.enums.CardType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CardDTO {
    private String id;

    private String cardNumber;

    private String cardHolderName;

    private String expirationDate;

    @Deprecated(since = "1.0.0")
    private String cvv; //TODO: Deprecated

    private LocalDateTime expiryDate;

    private CardType type;

    private CardStatus status;

    private CardNetwork network;

    private String lastFourDigits;

    private Double limitAmount;

    private Double availableAmount;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}