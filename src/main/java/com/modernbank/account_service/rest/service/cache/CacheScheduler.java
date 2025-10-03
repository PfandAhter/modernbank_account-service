package com.modernbank.account_service.rest.service.cache;

import com.modernbank.account_service.rest.service.cache.account.IAccountCacheService;
import com.modernbank.account_service.rest.service.cache.error.IErrorCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheScheduler {

    private final IErrorCacheService errorCacheService;

    private final IAccountCacheService accountCacheService;

    @Scheduled(cron = "${cache.refresh.cronErrorCode:0 0 1 * * SUN}") // Default value is 1 AM every Sunday
    public void refreshErrorCodesCache(){
        log.info("Refreshing error codes cache");
        try{
            errorCacheService.refreshErrorCodesCache();
            log.info("Error codes cache refreshed successfully");
        }catch (Exception e){
            log.error("Error occurred while refreshing error codes cache", e);
        }
    }

    @Scheduled(cron =  "${cache.refresh.accounts:0 */5 * * * *}") // Default value is every 5 minutes
    public void refreshAccountsCache(){
        log.info("Refreshing accounts cache");
        try{
            accountCacheService.refreshAccountsByUserId();
            log.info("Accounts cache refreshed successfully");
        }catch (Exception e){
            log.error("Error occurred while refreshing accounts cache", e);
        }
    }
}