package com.modernbank.account_service.rest.controller;

import com.modernbank.account_service.api.dto.CityDTO;
import com.modernbank.account_service.api.dto.DistrictDTO;
import com.modernbank.account_service.api.response.GetActiveBranchCitiesResponse;
import com.modernbank.account_service.api.response.GetActiveBranchDistrictsResponse;
import com.modernbank.account_service.api.BranchControllerApi;
import com.modernbank.account_service.api.request.CreateBranchRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.rest.service.IMapperService;
import com.modernbank.account_service.rest.service.IBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/branch")
@RequiredArgsConstructor
@CrossOrigin
public class BranchServiceController implements BranchControllerApi {

    private final IBranchService branchService;

    private final IMapperService mapperService;

    @Override
    public BaseResponse createBranch(CreateBranchRequest createBranchRequest) {
        branchService.createBranch(createBranchRequest);
        return new BaseResponse("Branch created successfully");
    }

    @Override
    public GetActiveBranchCitiesResponse getAllBranches() {
        return new GetActiveBranchCitiesResponse(mapperService.map(branchService.getAllCities(), CityDTO.class));
    }

    @Override
    public GetActiveBranchDistrictsResponse getAllDistrictsByCity(Long cityId) {
        return new GetActiveBranchDistrictsResponse(mapperService.map(branchService.getDistrictsByCityId(cityId), DistrictDTO.class));
    }
}