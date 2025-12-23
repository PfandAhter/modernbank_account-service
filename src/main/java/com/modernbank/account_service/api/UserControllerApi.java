package com.modernbank.account_service.api;

import com.modernbank.account_service.api.request.*;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.api.response.GetSavedAccountsResponse;
import com.modernbank.account_service.api.response.GetUserInfoResponse;
import com.modernbank.account_service.api.response.UserDetailsResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserControllerApi {

    @PostMapping(path = "/create", produces = "application/json", consumes = "application/json")
    BaseResponse createUser(@RequestBody CreateUserRequest createUserRequest);

    @PostMapping(path = "/update")
    BaseResponse updateUser(@RequestBody UpdateUserRequest updateUserRequest);

    @PostMapping(path = "/saved-accounts/add")
    BaseResponse addSavedAccount(@RequestBody AddSavedAccountRequest request);

    @PostMapping(path = "/saved-accounts/get")
    GetSavedAccountsResponse getSavedAccounts(@RequestBody GetSavedAccountsRequest request);

    @PostMapping(path = "/saved-accounts/remove")
    BaseResponse removeSavedAccount(@RequestBody RemoveSavedAccountRequest request);

    @PostMapping(path = "/get/info")
    GetUserInfoResponse getUserInfo(@RequestBody BaseRequest baseRequest);

    @PostMapping(path = "/update/info")
    BaseResponse updateUserInfo(@RequestBody UpdateUserInfoRequest updateUserInfoRequest, HttpServletRequest request);

    @GetMapping(path = "/auth-details")
    UserDetailsResponse getUserAuthDetails(@RequestParam(value = "email")String email);
}