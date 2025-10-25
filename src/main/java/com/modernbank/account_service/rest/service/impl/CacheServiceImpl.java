package com.modernbank.account_service.rest.service.impl;

import com.modernbank.account_service.rest.service.CacheService;
import com.modernbank.account_service.rest.service.cache.account.IAccountCacheService;
import com.modernbank.account_service.rest.service.cache.district.CityDistrictCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheServiceImpl implements CacheService {

    private final IAccountCacheService accountCacheService;

    private final CityDistrictCacheService cityDistrictCacheService;

    @Override
    public void refreshAccountCache() {
        accountCacheService.refreshAccountsByUserId();
    }

    @Override
    public void refreshCityCache() {
        cityDistrictCacheService.evictCityCacheValues();
    }

    @Override
    public void refreshDistrictCache() {
        cityDistrictCacheService.evictDistrictCacheValues();
    }
}