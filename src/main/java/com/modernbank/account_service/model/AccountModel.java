package com.modernbank.account_service.model;

import com.modernbank.account_service.model.enums.Currency;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AccountModel {
    private String id;

    private String iban;

    private String name;

    private double balance;

    private String firstName;

    private String userId;

    private String secondName;

    private String lastName;

    private Currency currency;
}