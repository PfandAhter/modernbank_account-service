package com.modernbank.account_service.rest.service;

import com.modernbank.account_service.api.request.CreateUserRequest;
import com.modernbank.account_service.api.request.UpdateUserInfoRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.model.GetUserModel;

public interface UserService {

    BaseResponse createUser(CreateUserRequest request);

    GetUserModel getUserInfo(String email);

    void updateUserInfo(UpdateUserInfoRequest request);
}