package com.modernbank.account_service.rest.service.cache;

import com.modernbank.account_service.rest.service.cache.account.IAccountCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheScheduler {

    private final IAccountCacheService accountCacheService;

    @Scheduled(cron =  "${cache.refresh.accounts:0 */5 * * * *}") // Default value is every 5 minutes
    public void refreshAccountsCache(){
        log.info("Refreshing accounts cache");
        try{
            accountCacheService.refreshAccountsByUserId();
            log.info("Accounts cache refreshed successfully");
        }catch (Exception e){
            log.error("Error occurred while refreshing accounts cache");
        }
    }
}