package com.modernbank.account_service.rest.controller;

import com.modernbank.account_service.api.CityDistrictControllerApi;
import com.modernbank.account_service.api.request.citydistrict.CreateCityRequest;
import com.modernbank.account_service.api.request.citydistrict.CreateDistrictRequest;
import com.modernbank.account_service.api.request.citydistrict.UpdateCityRequest;
import com.modernbank.account_service.api.request.citydistrict.UpdateDistrictRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.rest.service.CityDistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/city-districts")
@RequiredArgsConstructor

//TODO: Burada city ve district islemleri icin yetki kontrolu eklenecektir...
public class CityDistrictController implements CityDistrictControllerApi {

    private final CityDistrictService cityDistrictService;

    @Override
    public BaseResponse createCity(CreateCityRequest createCityRequest) {
        cityDistrictService.createCity(createCityRequest);
        return new BaseResponse("City created successfully");
    }

    @Override
    public BaseResponse createDistrict(CreateDistrictRequest createCityRequest) {
        cityDistrictService.createDistrict(createCityRequest);
        return new BaseResponse("District created successfully");
    }

    @Override
    public BaseResponse updateDistrict(UpdateDistrictRequest updateDistrictRequest) {
        cityDistrictService.updateDistrict(updateDistrictRequest);
        return new BaseResponse("District updated successfully");
    }

    @Override
    public BaseResponse updateCity(UpdateCityRequest updateCityRequest) {
        cityDistrictService.updateCity(updateCityRequest);
        return new BaseResponse("City updated successfully");
    }
}