package com.modernbank.account_service.rest.controller;

import com.modernbank.account_service.api.UserControllerApi;
import com.modernbank.account_service.api.dto.GetUserDTO;
import com.modernbank.account_service.api.request.CreateUserRequest;
import com.modernbank.account_service.api.request.UpdateUserRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.api.response.GetUserInfoResponse;
import com.modernbank.account_service.rest.service.MapperService;
import com.modernbank.account_service.rest.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserServiceController implements UserControllerApi {

    private final IUserService userService;

    private final MapperService mapperService;

    @Override
    public ResponseEntity<BaseResponse> createUser(CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(userService.createUser(createUserRequest));
    }

    @Override
    public BaseResponse updateUser(UpdateUserRequest updateUserRequest) {

        return new BaseResponse("Update User Success");
    }

    @Override
    public GetUserInfoResponse getUserInfo(HttpServletRequest httpServletRequest) {
        return new GetUserInfoResponse(mapperService.map(userService.getUserInfo(httpServletRequest.getHeader("X-User-Email")), GetUserDTO.class));
    }
}