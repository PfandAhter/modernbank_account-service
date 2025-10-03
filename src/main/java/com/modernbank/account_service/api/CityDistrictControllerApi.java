package com.modernbank.account_service.api;

import com.modernbank.account_service.api.request.citydistrict.CreateCityRequest;
import com.modernbank.account_service.api.request.citydistrict.CreateDistrictRequest;
import com.modernbank.account_service.api.request.citydistrict.UpdateCityRequest;
import com.modernbank.account_service.api.request.citydistrict.UpdateDistrictRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CityDistrictControllerApi {
    @PostMapping(path = "/city/create")
    BaseResponse createCity(@RequestBody CreateCityRequest createCityRequest);

    @PostMapping(path = "/district/create")
    BaseResponse createDistrict(@RequestBody CreateDistrictRequest createCityRequest);

    @PutMapping(path = "/district/update")
    BaseResponse updateDistrict(@RequestBody UpdateDistrictRequest updateDistrictRequest);

    @PutMapping(path = "/city/update")
    BaseResponse updateCity(@RequestBody UpdateCityRequest updateCityRequest);
}