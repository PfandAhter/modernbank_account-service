package com.modernbank.account_service.api.response;

import com.modernbank.account_service.model.enums.AccountStatus;
import com.modernbank.account_service.model.enums.Currency;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class GetAccountByIBAN extends BaseResponse{

    private String accountId;

    private String accountName;

    private String userId;

    private String firstName;

    private String secondName;

    private String email;

    private String tckn;

    private double balance;

    private Double dailyTransferLimit;

    private Double dailyDepositLimit;

    private Double dailyWithdrawLimit;

    private String lastName;

    private Currency currency;

    private AccountStatus accountStatus;
}