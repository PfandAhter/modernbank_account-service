package com.modernbank.account_service.rest.controller;

import com.modernbank.account_service.rest.controller.api.UserControllerApi;
import com.modernbank.account_service.rest.controller.api.request.CreateUserRequest;
import com.modernbank.account_service.rest.controller.api.response.BaseResponse;
import com.modernbank.account_service.rest.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserServiceController implements UserControllerApi {

    private final IUserService userService;

    @Override
    public ResponseEntity<BaseResponse> createUser(CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(userService.createUser(createUserRequest));
    }
}