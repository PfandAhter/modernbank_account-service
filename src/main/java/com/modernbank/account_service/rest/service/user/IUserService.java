package com.modernbank.account_service.rest.service.user;

import com.modernbank.account_service.rest.controller.api.request.CreateUserRequest;
import com.modernbank.account_service.rest.controller.api.response.BaseResponse;

public interface IUserService {

    BaseResponse createUser(CreateUserRequest request);
}