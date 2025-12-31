package com.modernbank.account_service.rest.service;

import com.modernbank.account_service.api.request.CreateAccountRequest;
import com.modernbank.account_service.api.request.GetAccountDetailsRequest;
import com.modernbank.account_service.api.request.account.AddIBANBlackListRequest;
import com.modernbank.account_service.api.request.account.RemoveIBANBlackListRequest;
import com.modernbank.account_service.api.response.AccountProfileResponse;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.api.response.GetAccountByIBAN;
import com.modernbank.account_service.api.response.GetAccountOwnerNameResponse;
import com.modernbank.account_service.entity.Account;
import com.modernbank.account_service.entity.Card;
import com.modernbank.account_service.model.AccountListModel;
import com.modernbank.account_service.model.AccountModel;

public interface AccountService {
    BaseResponse createAccount(CreateAccountRequest request);

    AccountListModel getAccountsByUser(String userId);

    AccountModel getAccountDetails(GetAccountDetailsRequest request);

    GetAccountByIBAN getAccountByIBAN(String iban);

    AccountModel getAccountById(String accountId);

    GetAccountOwnerNameResponse getAccountOwnerName(String iban);

    BaseResponse updateBalance(String iban, double balance);

    void updateAccountLimit(String accountId, double amount, String category);

    AccountProfileResponse getAccountProfile(String accountId);

    BaseResponse holdAccount(String accountId);

    Boolean isReceiverBlacklisted(String iban);

    Boolean validateAccountNotBlocked(String accountId);

    BaseResponse incrementFraudCount(String accountId, String reason);

    void updatePreviousFraudFlag(String accountId, boolean flag);

    void addIBANBlackList(AddIBANBlackListRequest request);

    void removeIBANBlackList(RemoveIBANBlackListRequest request);
}