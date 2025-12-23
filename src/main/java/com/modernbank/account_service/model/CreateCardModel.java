package com.modernbank.account_service.model;

import com.modernbank.account_service.entity.Account;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCardModel {
    private String cardHolderName;

    private String cardType;

    private String cardNetwork;

    private String accountId;

    private Account account;
}