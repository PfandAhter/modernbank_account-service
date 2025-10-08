package com.modernbank.account_service.rest.controller;

import com.modernbank.account_service.api.dto.BranchDTO;
import com.modernbank.account_service.api.dto.CityDTO;
import com.modernbank.account_service.api.dto.DistrictDTO;
import com.modernbank.account_service.api.request.UpdateBranchRequest;
import com.modernbank.account_service.api.response.GetActiveBranchCitiesResponse;
import com.modernbank.account_service.api.response.GetActiveBranchDistrictsResponse;
import com.modernbank.account_service.api.BranchControllerApi;
import com.modernbank.account_service.api.request.CreateBranchRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.api.response.GetActiveBranchesResponse;
import com.modernbank.account_service.rest.service.MapperService;
import com.modernbank.account_service.rest.service.IBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/branch")
@RequiredArgsConstructor
public class BranchServiceController implements BranchControllerApi {

    private final IBranchService branchService;

    private final MapperService mapperService;

    @Override
    public BaseResponse createBranch(CreateBranchRequest createBranchRequest) {
        branchService.createBranch(createBranchRequest);
        return new BaseResponse("Branch created successfully");
    }

    @Override
    public BaseResponse updateBranch(UpdateBranchRequest updateBranchRequest) {
        branchService.updateBranch(updateBranchRequest);
        return new BaseResponse("Branch updated successfully");
    }

    @Override
    public GetActiveBranchCitiesResponse getAllBranches() {
        return new GetActiveBranchCitiesResponse(mapperService.map(branchService.getAllCities(), CityDTO.class));
    }

    @Override
    public GetActiveBranchDistrictsResponse getAllDistrictsByCity(Long cityId) {
        return new GetActiveBranchDistrictsResponse(mapperService.map(branchService.getDistrictsByCityId(cityId), DistrictDTO.class));
    }

    @Override
    public GetActiveBranchesResponse getAllActiveBranchesByDistrict(Long districtId) {
        return new GetActiveBranchesResponse(mapperService.map(branchService.getAllBranchesByDistrictId(districtId), BranchDTO.class));
    }
}