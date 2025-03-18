package com.modernbank.account_service.rest.service.cache.account;

import com.modernbank.account_service.model.dto.AccountDTO;
import com.modernbank.account_service.repository.AccountRepository;
import com.modernbank.account_service.rest.service.IMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountCacheServiceImpl {

    private final AccountRepository accountRepository;

    private final IMapperService mapperService;

    @Cacheable(value = "account", key = "#userId")
    public List<AccountDTO> getAccountsByUserId(String userId) {
        return mapperService.map(accountRepository.findAccountByUserId(userId), AccountDTO.class);
    }
}