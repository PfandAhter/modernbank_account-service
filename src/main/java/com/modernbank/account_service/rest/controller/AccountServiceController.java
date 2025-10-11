package com.modernbank.account_service.rest.controller;

import com.modernbank.account_service.api.AccountControllerApi;
import com.modernbank.account_service.api.request.BaseRequest;
import com.modernbank.account_service.api.request.CreateAccountRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.api.response.GetAccountByIBAN;
import com.modernbank.account_service.api.response.GetAccountOwnerNameResponse;
import com.modernbank.account_service.api.response.GetAccountsResponse;
import com.modernbank.account_service.rest.service.AccountService;
import com.modernbank.account_service.rest.service.MapperService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountServiceController implements AccountControllerApi {

    private final AccountService accountService;

    private final MapperService mapperService;

    @Override
    public ResponseEntity<BaseResponse> createAccount(CreateAccountRequest createAccountRequest) {
        return ResponseEntity.ok(accountService.createAccount(createAccountRequest));
    }

    @Override
    public GetAccountsResponse getAccounts(String userId, HttpServletRequest request) {
        if (userId == null) {
            return mapperService.map(accountService.getAccountsByUser(request.getHeader("X-User-Id")), GetAccountsResponse.class);
        }
        return mapperService.map(accountService.getAccountsByUser(userId), GetAccountsResponse.class);
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