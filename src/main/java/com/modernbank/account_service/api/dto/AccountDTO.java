package com.modernbank.account_service.api.dto;

import com.modernbank.account_service.model.enums.Currency;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AccountDTO {

    private String id;

    private String iban;

    private String name;

    private double balance;

    private Currency currency;
}