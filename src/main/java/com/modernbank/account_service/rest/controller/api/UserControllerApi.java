package com.modernbank.account_service.rest.controller.api;

import com.modernbank.account_service.rest.controller.api.request.CreateUserRequest;
import com.modernbank.account_service.rest.controller.api.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserControllerApi {

    @PostMapping(path = "/create", produces = "application/json", consumes = "application/json")
    ResponseEntity<BaseResponse> createUser(@RequestBody CreateUserRequest createUserRequest);
}
