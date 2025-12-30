package com.modernbank.account_service.rest.controller;

import com.modernbank.account_service.api.CacheControllerApi;
import com.modernbank.account_service.api.request.BaseRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.rest.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cache/admin")
@RequiredArgsConstructor

public class CacheServiceController implements CacheControllerApi {

    private final CacheService cacheService;

    @Override
    public BaseResponse refreshAccountCache(BaseRequest baseRequest) {
        cacheService.refreshAccountCache();
        return new BaseResponse("Account cache refreshed successfully");
    }

    @Override
    public BaseResponse refreshCityCache(BaseRequest baseRequest) {
        cacheService.refreshCityCache();
        return new BaseResponse("City cache refreshed successfully");
    }

    @Override
    public BaseResponse refreshDistrictCache(BaseRequest baseRequest) {
        cacheService.refreshDistrictCache();
        return new BaseResponse("District cache refreshed successfully");
    }

    @Override
    public BaseResponse refreshErrorCodeCache(BaseRequest baseRequest) {
        cacheService.refreshErrorCodesCache();
        return new BaseResponse("Error code cache refreshed successfully");
    }
}
