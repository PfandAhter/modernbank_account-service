package com.modernbank.account_service.rest.service;

import com.modernbank.account_service.api.request.*;
import com.modernbank.account_service.model.GetUserModel;
import com.modernbank.account_service.model.SavedAccountModel;

import java.util.List;

public interface UserService {

    void createUser(CreateUserRequest request);

    GetUserModel getUserInfo(String email);

    void updateUserInfo(UpdateUserInfoRequest request);

    void addSavedAccount(AddSavedAccountRequest request);

    void removeSavedAccount(RemoveSavedAccountRequest request);

    List<SavedAccountModel> getSavedAccounts(GetSavedAccountsRequest request);
}