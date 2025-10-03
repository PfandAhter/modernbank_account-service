package com.modernbank.account_service.rest.service;

import com.modernbank.account_service.model.CityModel;
import com.modernbank.account_service.model.DistrictModel;
import com.modernbank.account_service.api.request.CreateBranchRequest;
import com.modernbank.account_service.api.response.BaseResponse;

import java.util.List;

public interface IBranchService {

    void createBranch(CreateBranchRequest request);

    List<CityModel> getAllCities();

    List<DistrictModel> getDistrictsByCityId(Long cityId);
}