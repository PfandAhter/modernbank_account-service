package com.modernbank.account_service.rest.service;

import com.modernbank.account_service.api.request.citydistrict.CreateCityRequest;
import com.modernbank.account_service.api.request.citydistrict.CreateDistrictRequest;
import com.modernbank.account_service.api.request.citydistrict.UpdateCityRequest;
import com.modernbank.account_service.api.request.citydistrict.UpdateDistrictRequest;
import com.modernbank.account_service.entity.City;
import com.modernbank.account_service.entity.District;

public interface CityDistrictService {

    void createCity(CreateCityRequest request);

    void createDistrict(CreateDistrictRequest request);

    void updateDistrict(UpdateDistrictRequest request);

    void updateCity(UpdateCityRequest request);

    City getCityById(Long cityId);

    District getDistrictById(Long districtId);
}