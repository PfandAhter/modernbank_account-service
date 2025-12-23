package com.modernbank.account_service.rest.service.cache.account;

import com.modernbank.account_service.api.dto.AccountDTO;
import com.modernbank.account_service.entity.Account;

import java.util.List;

public interface IAccountCacheService {

    List<AccountDTO> getAccountsByUserId(String userId);

    void updateAccount(Account account);

    void refreshAccountsByUserId();
}