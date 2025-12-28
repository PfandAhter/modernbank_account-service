package com.modernbank.account_service.rest.controller;

import com.modernbank.account_service.api.AccountControllerApi;
import com.modernbank.account_service.api.dto.AccountDTO;
import com.modernbank.account_service.api.request.BaseRequest;
import com.modernbank.account_service.api.request.CreateAccountRequest;
import com.modernbank.account_service.api.request.GetAccountDetailsRequest;
import com.modernbank.account_service.api.request.account.AddIBANBlackListRequest;
import com.modernbank.account_service.api.request.account.RemoveIBANBlackListRequest;
import com.modernbank.account_service.api.response.*;
import com.modernbank.account_service.rest.service.AccountService;
import com.modernbank.account_service.rest.service.MapperService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
            return mapperService.map(accountService.getAccountsByUser(request.getHeader("X-User-Id")),
                    GetAccountsResponse.class);
        }
        return mapperService.map(accountService.getAccountsByUser(userId), GetAccountsResponse.class);
    }

    @Override
    public GetAccountDetailsResponse getAccountDetails(GetAccountDetailsRequest request) {
        return new GetAccountDetailsResponse(
                mapperService.map(accountService.getAccountDetails(request), AccountDTO.class));
    }

    @Override
    public GetAccountsResponse getAccountsV2(BaseRequest baseRequest) {
        return mapperService.map(accountService.getAccountsByUser(baseRequest.getUserId()), GetAccountsResponse.class);
    }

    @Override
    public ResponseEntity<GetAccountByIBAN> getAccountByIBAN(String iban) {
        return ResponseEntity.ok(accountService.getAccountByIBAN(iban));
    }

    @Override
    public ResponseEntity<GetAccountByIdResponse> getAccountByAccountId(String accountId) {
        return ResponseEntity.ok(new GetAccountByIdResponse(mapperService.map(accountService.getAccountById(accountId), AccountDTO.class)));
    }

    @Override
    public ResponseEntity<GetAccountOwnerNameResponse> getAccountOwnerName(String iban) {
        return ResponseEntity.ok(accountService.getAccountOwnerName(iban));
    }

    @Override
    public ResponseEntity<BaseResponse> updateBalance(String iban, double balance) {
        return ResponseEntity.ok(accountService.updateBalance(iban, balance));
    }

    // Fraud detection endpoints

    @Override
    public ResponseEntity<AccountProfileResponse> getAccountProfileByAccountId(String accountId) {
        return ResponseEntity.ok(accountService.getAccountProfile(accountId));
    }

    @Override
    public ResponseEntity<BaseResponse> holdAccount(String accountId) {
        return ResponseEntity.ok(accountService.holdAccount(accountId));
    }

    @Override
    public ResponseEntity<Boolean> isReceiverBlacklisted(String iban) {
        return ResponseEntity.ok(accountService.isReceiverBlacklisted(iban));
    }

    // Soft block endpoints
    @Override
    public Boolean validateAccountNotBlocked(String accountId) {
        return accountService.validateAccountNotBlocked(accountId);
    }

    @Override
    public ResponseEntity<BaseResponse> incrementFraudCount(String accountId, String reason) {
        return ResponseEntity.ok(accountService.incrementFraudCount(accountId,reason));
    }

    @Override
    public ResponseEntity<BaseResponse> updateFraudFlag(String accountId, Boolean isFraud) {
        accountService.updatePreviousFraudFlag(accountId,isFraud);
        return ResponseEntity.ok(new BaseResponse("Fraud flag updated successfully."));
    }

    @Override
    public ResponseEntity<BaseResponse> addToBlacklist(AddIBANBlackListRequest request) {
        accountService.addIBANBlackList(request);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse("IBAN: " + request.getIban() + ", " + request.getUserId() +" tarafindan kara listeye eklendi."));
    }

    @Override
    public ResponseEntity<BaseResponse> removeFromBlacklist(RemoveIBANBlackListRequest request) {
        accountService.removeIBANBlackList(request);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse("IBAN: " + request.getIban() + ", " + request.getUserId() + " tarafindan kara listeden çıkarıldı."));
    }
}