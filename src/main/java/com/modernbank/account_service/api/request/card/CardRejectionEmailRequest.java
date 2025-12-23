package com.modernbank.account_service.api.request.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRejectionEmailRequest {
    private String userEmail;
    private String cardHolderName;
    private String cardType;
    private String applicationDate;
    private String rejectionReason;
}