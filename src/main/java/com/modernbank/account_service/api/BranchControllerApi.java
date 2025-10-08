package com.modernbank.account_service.api;

import com.modernbank.account_service.api.request.UpdateBranchRequest;
import com.modernbank.account_service.api.response.GetActiveBranchCitiesResponse;
import com.modernbank.account_service.api.response.GetActiveBranchDistrictsResponse;
import com.modernbank.account_service.api.request.CreateBranchRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import com.modernbank.account_service.api.response.GetActiveBranchesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface BranchControllerApi {

    @PostMapping(path = "/create", produces = "application/json", consumes = "application/json")
    BaseResponse createBranch(@RequestBody CreateBranchRequest createBranchRequest);

    @PutMapping(path = "/update")
    BaseResponse updateBranch(@RequestBody UpdateBranchRequest updateBranchRequest);

    @GetMapping(path = "/cities")
    GetActiveBranchCitiesResponse getAllBranches();

    @GetMapping(path = "/districts")
    GetActiveBranchDistrictsResponse getAllDistrictsByCity(@RequestParam("city") Long cityId);

    @GetMapping(path = "/branches")
    GetActiveBranchesResponse getAllActiveBranchesByDistrict(@RequestParam("district") Long districtId);
}