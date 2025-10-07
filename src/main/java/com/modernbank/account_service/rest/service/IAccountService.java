package com.modernbank.account_service.rest.service;


import com.modernbank.account_service.api.request.BaseRequest;
import com.modernbank.account_service.api.request.CreateAccountRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.api.response.GetAccountByIBAN;
import com.modernbank.account_service.api.response.GetAccountOwnerNameResponse;
import com.modernbank.account_service.api.response.GetAccountsResponse;

public interface IAccountService {
    BaseResponse createAccount(CreateAccountRequest request);

    GetAccountsResponse getAccountsByUser(BaseRequest request);

    GetAccountByIBAN getAccountByIBAN (String iban);

    GetAccountOwnerNameResponse getAccountOwnerName (String iban);

    BaseResponse updateBalance(String iban, double balance);
}