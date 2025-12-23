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

    private String cvv;

    private LocalDateTime expiryDate;

    private CardType type;

    private CardStatus status;

    private CardNetwork network;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}