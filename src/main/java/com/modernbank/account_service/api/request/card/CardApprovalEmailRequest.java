package com.modernbank.account_service.api.request.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardApprovalEmailRequest {
    private String userEmail;
    private String cardHolderName;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String cardType;
    private String cardNetwork;
    private Double creditLimit;
}
