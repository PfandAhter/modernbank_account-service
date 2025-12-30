package com.modernbank.account_service.rest.service;

import com.modernbank.account_service.api.request.*;
import com.modernbank.account_service.model.GetUserModel;
import com.modernbank.account_service.model.SavedAccountModel;
import com.modernbank.account_service.model.UserDetailsModel;

import java.util.List;

public interface UserService {

    void createUser(CreateUserRequest request);

    GetUserModel getUserInfo(String email);

    void updateUserInfo(UpdateUserInfoRequest request);

    void addSavedAccount(AddSavedAccountRequest request);

    void removeSavedAccount(RemoveSavedAccountRequest request);

    void updateUserRole(AdminUpdateUserRoleRequest request);

    void deleteUser(AdminDeleteUserRequest request);

    List<SavedAccountModel> getSavedAccounts(GetSavedAccountsRequest request);

    UserDetailsModel getUserDetailsByEmail(String email);
}