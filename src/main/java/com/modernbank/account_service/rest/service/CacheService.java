package com.modernbank.account_service.rest.service;

public interface CacheService {

    void refreshAccountCache();

    void refreshCityCache();

    void refreshDistrictCache();
}