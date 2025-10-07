package com.modernbank.account_service.rest.controller;

import com.modernbank.account_service.api.AccountControllerApi;
import com.modernbank.account_service.api.request.BaseRequest;
import com.modernbank.account_service.api.request.CreateAccountRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.api.response.GetAccountByIBAN;
import com.modernbank.account_service.api.response.GetAccountOwnerNameResponse;
import com.modernbank.account_service.api.response.GetAccountsResponse;
import com.modernbank.account_service.rest.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@CrossOrigin
public class AccountServiceController implements AccountControllerApi {

    private final IAccountService accountService;

    @Override
    public ResponseEntity<BaseResponse> createAccount(CreateAccountRequest createAccountRequest) {
        return ResponseEntity.ok(accountService.createAccount(createAccountRequest));
    }

    @Override
    public ResponseEntity<GetAccountsResponse> getAccounts(BaseRequest baseRequest) {
        return ResponseEntity.ok(accountService.getAccountsByUser(baseRequest));
    }

    @Override
    public ResponseEntity<GetAccountByIBAN> getAccountByIBAN(String iban) {
        return ResponseEntity.ok(accountService.getAccountByIBAN(iban));
    }

    @Override
    public ResponseEntity<GetAccountOwnerNameResponse> getAccountOwnerName(String iban) {
        return ResponseEntity.ok(accountService.getAccountOwnerName(iban));
    }

    @Override
    public ResponseEntity<BaseResponse> updateBalance(String iban, double balance) {
        return ResponseEntity.ok(accountService.updateBalance(iban, balance));
    }
}