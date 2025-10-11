package com.modernbank.account_service.rest.service.cache.account;

import com.modernbank.account_service.api.dto.AccountDTO;
import com.modernbank.account_service.repository.AccountRepository;
import com.modernbank.account_service.rest.service.MapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountCacheServiceImpl implements IAccountCacheService {

    private final AccountRepository accountRepository;

    private final MapperService mapperService;

    @Cacheable(value = "account", key = "#userId")
    @Override
    public List<AccountDTO> getAccountsByUserId(String userId) {
        return mapperService.map(accountRepository.findAccountByUserId(userId), AccountDTO.class);
    }

    @CacheEvict(value = "account", allEntries = true)
    @Override
    public void refreshAccountsByUserId(){
        // This method will evict all entries in the cache, effectively refreshing it.
        // You can also implement a more specific eviction strategy if needed.
        log.info("Cache for accounts has been refreshed.");
    }
}