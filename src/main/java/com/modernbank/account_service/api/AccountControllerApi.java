package com.modernbank.account_service.api;

import com.modernbank.account_service.api.request.BaseRequest;
import com.modernbank.account_service.api.request.CreateAccountRequest;
import com.modernbank.account_service.api.request.GetAccountDetailsRequest;
import com.modernbank.account_service.api.request.account.AddIBANBlackListRequest;
import com.modernbank.account_service.api.request.account.RemoveIBANBlackListRequest;
import com.modernbank.account_service.api.response.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface AccountControllerApi {

    @PostMapping(path = "/create", produces = "application/json", consumes = "application/json")
    ResponseEntity<BaseResponse> createAccount(@RequestBody CreateAccountRequest createAccountRequest);

    @GetMapping(path = "/get")
    GetAccountsResponse getAccounts(@RequestHeader("X-User-Id") String userId, HttpServletRequest request);

    @PostMapping(path = "/get-details")
    GetAccountDetailsResponse getAccountDetails(@RequestBody GetAccountDetailsRequest request);

    @PostMapping(path = "/getv2")
    GetAccountsResponse getAccountsV2(@RequestBody BaseRequest baseRequest);

    @GetMapping(path = "/get-by-iban")
    ResponseEntity<GetAccountByIBAN> getAccountByIBAN(@RequestParam(value = "iban") String iban);

    @GetMapping(path = "/get-by-accountId")
    ResponseEntity<GetAccountByIdResponse> getAccountByAccountId(@RequestParam(value = "accountId") String accountId);

    @GetMapping(path = "/get/user/by-iban")
    ResponseEntity<GetAccountOwnerNameResponse> getAccountOwnerName(@RequestParam(value = "iban") String iban);

    @PostMapping(path = "/balance/update")
    ResponseEntity<BaseResponse> updateBalance(@RequestParam(value = "iban") String iban,
            @RequestParam(value = "balance") double balance);

    @PostMapping(path = "/limit/update")
    ResponseEntity<BaseResponse> updateAccountLimit(
            @RequestParam(value = "accountId") String accountId,
            @RequestParam(value = "amount") Double amount,
            @RequestParam(value = "category") String category
    );

    @GetMapping(path = "/profile")
    ResponseEntity<AccountProfileResponse> getAccountProfileByAccountId(@RequestParam(value = "accountId") String accountId);

    @PostMapping(path = "/hold")
    ResponseEntity<BaseResponse> holdAccount(@RequestParam(value = "accountId") String accountId);

    @GetMapping(path = "/blacklist/check")
    ResponseEntity<Boolean> isReceiverBlacklisted(@RequestParam(value = "iban") String iban);

    @GetMapping(path = "/blocked/check")
    Boolean validateAccountNotBlocked(@RequestParam(value = "accountId") String accountId);

    @PostMapping(path = "/fraud/confirm")
    ResponseEntity<BaseResponse> incrementFraudCount(@RequestParam(value = "accountId") String accountId, @RequestParam(value = "reason") String reason);

    @PostMapping(path = "/fraud/flag/update")
    ResponseEntity<BaseResponse> updateFraudFlag(@RequestParam(value = "accountId") String accountId, @RequestParam(value = "isFraud") Boolean isFraud);

    @PostMapping(path = "/admin/blacklist/add")
    ResponseEntity<BaseResponse> addToBlacklist(@RequestBody AddIBANBlackListRequest addIBANBlackListRequest);

    @PostMapping(path = "/admin/blacklist/remove")
    ResponseEntity<BaseResponse> removeFromBlacklist(@RequestBody RemoveIBANBlackListRequest removeIBANBlackListRequest);
}