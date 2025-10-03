package com.modernbank.account_service.api;

import com.modernbank.account_service.api.response.GetActiveBranchCitiesResponse;
import com.modernbank.account_service.api.response.GetActiveBranchDistrictsResponse;
import com.modernbank.account_service.api.request.CreateBranchRequest;
import com.modernbank.account_service.api.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface BranchControllerApi {

    @PostMapping(path = "/create", produces = "application/json", consumes = "application/json")
    BaseResponse createBranch(@RequestBody CreateBranchRequest createBranchRequest);

    @GetMapping(path = "/cities")
    GetActiveBranchCitiesResponse getAllBranches();

    @GetMapping(path = "/districts")
    GetActiveBranchDistrictsResponse getAllDistrictsByCity(@RequestParam("city") Long cityId);



}