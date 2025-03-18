package com.modernbank.account_service.rest.service.cache;

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

    @Scheduled(cron = "${cache.refresh.cron:0 0 1 * * SUN}") // Default value is 1 AM every Sunday
    public void refreshErrorCodesCache(){
        log.info("Refreshing error codes cache");
        try{
            errorCacheService.refreshErrorCodesCache();
            log.info("Error codes cache refreshed successfully");
        }catch (Exception e){
            log.error("Error occurred while refreshing error codes cache", e);
        }
    }
}