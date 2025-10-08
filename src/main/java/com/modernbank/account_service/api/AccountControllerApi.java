package com.modernbank.account_service.api;

import com.modernbank.account_service.api.request.BaseRequest;
import com.modernbank.account_service.api.request.CreateAccountRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.api.response.GetAccountByIBAN;
import com.modernbank.account_service.api.response.GetAccountOwnerNameResponse;
import com.modernbank.account_service.api.response.GetAccountsResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface AccountControllerApi {

    @PostMapping(path = "/create", produces = "application/json", consumes = "application/json")
    ResponseEntity<BaseResponse> createAccount(@RequestBody CreateAccountRequest createAccountRequest);

    @GetMapping(path = "/get")
    GetAccountsResponse getAccounts(@RequestHeader("X-User-Id") String userId, HttpServletRequest request);

    @GetMapping(path = "/get-by-iban")
    ResponseEntity<GetAccountByIBAN> getAccountByIBAN(@RequestParam(value = "iban") String iban);

    @GetMapping(path = "/get/user/by-iban")
    ResponseEntity<GetAccountOwnerNameResponse> getAccountOwnerName(@RequestParam(value = "iban") String iban);

    @PostMapping(path = "/balance/update")
    ResponseEntity<BaseResponse> updateBalance(@RequestParam(value = "iban") String iban,
                                    @RequestParam(value = "balance") double balance);
}