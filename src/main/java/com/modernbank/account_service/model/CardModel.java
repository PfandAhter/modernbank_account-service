package com.modernbank.account_service.model;

import com.modernbank.account_service.model.enums.CardNetwork;
import com.modernbank.account_service.model.enums.CardStatus;
import com.modernbank.account_service.model.enums.CardType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardModel {

    private String id;

    private String cardNumber;

    private String cardHolderName;

    private String expirationDate;

    private String cvv;

    private LocalDateTime expiryDate;

    private Boolean pendingApproval;

    private CardType type;

    private CardStatus status;

    private CardNetwork network;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}