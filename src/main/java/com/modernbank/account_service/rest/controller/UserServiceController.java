package com.modernbank.account_service.rest.controller;

import com.modernbank.account_service.api.UserControllerApi;
import com.modernbank.account_service.api.dto.GetUserDTO;
import com.modernbank.account_service.api.dto.SavedAccountDTO;
import com.modernbank.account_service.api.dto.UserDetailsDTO;
import com.modernbank.account_service.api.request.*;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.api.response.GetSavedAccountsResponse;
import com.modernbank.account_service.api.response.GetUserInfoResponse;
import com.modernbank.account_service.api.response.UserDetailsResponse;
import com.modernbank.account_service.rest.service.MapperService;
import com.modernbank.account_service.rest.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserServiceController implements UserControllerApi {

    private final UserService userService;

    private final MapperService mapperService;

    @Override
    public BaseResponse createUser(CreateUserRequest createUserRequest) {
        userService.createUser(createUserRequest);
        return new BaseResponse("User Created Successfully");
    }

    @Override
    public BaseResponse updateUser(UpdateUserRequest updateUserRequest) {

        return new BaseResponse("Update User Success");
    }


    @Override
    public BaseResponse addSavedAccount(AddSavedAccountRequest request) {
        userService.addSavedAccount(request);
        return new BaseResponse("Saved Account Added Successfully");
    }

    @Override
    public GetSavedAccountsResponse getSavedAccounts(GetSavedAccountsRequest request) {
        return new GetSavedAccountsResponse(mapperService.map(userService.getSavedAccounts(request), SavedAccountDTO.class));
    }

    @Override
    public BaseResponse removeSavedAccount(RemoveSavedAccountRequest request) {
        userService.removeSavedAccount(request);
        return new BaseResponse("Saved Account Removed Successfully");
    }

    @Override
    public GetUserInfoResponse getUserInfo(BaseRequest request) {
        return new GetUserInfoResponse(mapperService.map(userService.getUserInfo(request.getUserEmail()), GetUserDTO.class));
    }

    @Override
    public BaseResponse updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest, HttpServletRequest request) {
        updateUserInfoRequest.setUserId(request.getHeader("X-User-Id"));
        userService.updateUserInfo(updateUserInfoRequest);
        return new BaseResponse("Update User Info Success");
    }

    @Override
    public UserDetailsResponse getUserAuthDetails(String email) {
        return new UserDetailsResponse(mapperService.map(userService.getUserDetailsByEmail(email), UserDetailsDTO.class));
    }
}