package com.modernbank.account_service.api;

import com.modernbank.account_service.api.request.CreateUserRequest;
import com.modernbank.account_service.api.request.UpdateUserInfoRequest;
import com.modernbank.account_service.api.request.UpdateUserRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.api.response.GetUserInfoResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserControllerApi {

    @PostMapping(path = "/create", produces = "application/json", consumes = "application/json")
    ResponseEntity<BaseResponse> createUser(@RequestBody CreateUserRequest createUserRequest);

    @PostMapping(path = "/update")
    BaseResponse updateUser(@RequestBody UpdateUserRequest updateUserRequest);

    @GetMapping(path = "/get/info")
    GetUserInfoResponse getUserInfo(HttpServletRequest httpServletRequest);

    @PostMapping(path = "/update/info")
    BaseResponse updateUserInfo(@RequestBody UpdateUserInfoRequest updateUserInfoRequest, HttpServletRequest request);
}
