package com.modernbank.account_service.rest.service;

import com.modernbank.account_service.api.request.UpdateBranchRequest;
import com.modernbank.account_service.model.BranchModel;
import com.modernbank.account_service.model.CityModel;
import com.modernbank.account_service.model.DistrictModel;
import com.modernbank.account_service.api.request.CreateBranchRequest;

import java.util.List;

public interface BranchService {

    void createBranch(CreateBranchRequest request);

    void updateBranch(UpdateBranchRequest request);

    List<CityModel> getAllCities();

    List<DistrictModel> getDistrictsByCityId(Long cityId);

    List<BranchModel> getAllBranchesByDistrictId(Long districtId);
}