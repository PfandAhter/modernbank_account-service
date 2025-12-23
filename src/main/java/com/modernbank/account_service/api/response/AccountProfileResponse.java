package com.modernbank.account_service.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountProfileResponse {

    private String accountId;
    private String userId;
    private String iban;
    private String accountName;

    private Integer accountAgeMonths;
    private Integer creditScore;
    private Integer previousFraudCount;

    private Boolean previousFraudFlag;
    private Integer cardAgeMonths;
    private String cardType;
    private Double currentBalance;

    private String accountStatus;
}
