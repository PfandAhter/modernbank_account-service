package com.modernbank.account_service.rest.service.cache.account;

import com.modernbank.account_service.model.dto.AccountDTO;

import java.util.List;

public interface IAccountCacheService {

    List<AccountDTO> getAccountsByUserId(String userId);

    void refreshAccountsByUserId();
}