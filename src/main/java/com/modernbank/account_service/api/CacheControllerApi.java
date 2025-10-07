package com.modernbank.account_service.api;

import com.modernbank.account_service.api.request.BaseRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CacheControllerApi {

    @PostMapping("/account/refresh")
    BaseResponse refreshAccountCache(@RequestBody BaseRequest baseRequest);

    @PostMapping("/city/refresh")
    BaseResponse refreshCityCache(@RequestBody BaseRequest baseRequest);

    @PostMapping("/district/refresh")
    BaseResponse refreshDistrictCache(@RequestBody BaseRequest baseRequest);
}